import re
import requests
from urllib import error, parse
from bs4 import BeautifulSoup
import os
import logging
import random

# 配置日志记录
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

# 随机用户代理列表
USER_AGENTS = [
    'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
    'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
    'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0'
]

def find_pictures(keyword, session):
    image_urls = []
    logging.info("搜索图片中...")
    for t in range(0, 1000, 60):
        # 正确编码URL参数
        params = {
            'word': keyword,
            'pn': t
        }
        url = f'https://image.baidu.com/search/flip?{parse.urlencode(params)}'
        try:
            resp = session.get(url, timeout=7, allow_redirects=False)
            pic_urls = re.findall('"objURL":"(.*?)",', resp.text, re.S)
            if not pic_urls:
                break
            image_urls.extend(pic_urls)
        except requests.exceptions.Timeout:
            logging.warning(f"请求超时：{url}")
        except requests.exceptions.RequestException as e:
            logging.error(f"请求出错：{url}, 错误信息：{e}")
    return image_urls

def download_images(urls, save_dir, limit):
    # 验证保存路径
    if not os.path.isabs(save_dir):
        save_dir = os.path.abspath(save_dir)
    os.makedirs(save_dir, exist_ok=True)
    for idx, img_url in enumerate(urls[:limit]):
        try:
            resp = requests.get(img_url, timeout=7)
            with open(os.path.join(save_dir, f"{idx}.jpg"), 'wb') as f:
                f.write(resp.content)
            logging.info(f"已下载第 {idx + 1} 张图片")
        except requests.exceptions.Timeout:
            logging.warning(f"下载超时：{img_url}")
        except requests.exceptions.RequestException as e:
            logging.error(f"下载失败：{img_url}, 错误信息：{e}")

def main():
    session = requests.Session()
    # 使用随机用户代理
    session.headers = {
        'User-Agent': random.choice(USER_AGENTS),
        'Accept-Language': 'zh-CN,zh;q=0.9',
    }
    keyword = input("请输入关键词：")
    try:
        limit = int(input("请输入要下载的数量："))
        if limit <= 0:
            raise ValueError("下载数量必须为正整数")
    except ValueError as e:
        logging.error(f"输入的下载数量无效：{e}")
        return
    save_dir = input("请输入保存路径：")
    # 验证保存路径是否包含非法字符
    if any(c in r'<>"|?*' for c in save_dir):
        logging.error("保存路径包含非法字符")
        return

    image_urls = find_pictures(keyword, session)
    logging.info(f"共找到 {len(image_urls)} 张图片")
    download_images(image_urls, save_dir, limit)

if __name__ == "__main__":
    main()
