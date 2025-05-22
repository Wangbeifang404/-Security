import re
import requests
from urllib import error
from bs4 import BeautifulSoup
import os

num = 0
numPicture = 0
file = ''
List = []

def find_pictures(keyword, session):
    image_urls = []
    print("搜索图片中...")
    for t in range(0, 1000, 60):
        url = f'https://image.baidu.com/search/flip?...&word={keyword}&pn={t}'
        try:
            resp = session.get(url, timeout=7, allow_redirects=False)
            pic_urls = re.findall('"objURL":"(.*?)",', resp.text, re.S)
            if not pic_urls:
                break
            image_urls.extend(pic_urls)
        except requests.exceptions.RequestException:
            continue
    return image_urls

def download_images(urls, save_dir, limit):
    os.makedirs(save_dir, exist_ok=True)
    for idx, img_url in enumerate(urls[:limit]):
        try:
            resp = requests.get(img_url, timeout=7)
            with open(os.path.join(save_dir, f"{idx}.jpg"), 'wb') as f:
                f.write(resp.content)
            print(f"已下载第 {idx + 1} 张图片")
        except requests.exceptions.RequestException:
            print(f"下载失败：{img_url}")

def main():
    session = requests.Session()
    session.headers = {
        'User-Agent': 'Mozilla/5.0 ...',
        'Accept-Language': 'zh-CN,zh;q=0.9',
    }
    keyword = input("请输入关键词：")
    limit = int(input("请输入要下载的数量："))
    save_dir = input("请输入保存路径：")

    image_urls = find_pictures(keyword, session)
    print(f"共找到 {len(image_urls)} 张图片")
    download_images(image_urls, save_dir, limit)

if __name__ == "__main__":
    main()
