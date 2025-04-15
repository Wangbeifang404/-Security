# 🔐 加密算法工具包

这是一个自编的 Python 加密脚本，实现了多种经典和现代加密算法，包括对称加密、密钥扩展、哈希函数和 HMAC 操作等。

## 📦 功能概览

### ✅ 支持的算法

- **AES 加密（CBC 模式）**
- **SM4 加密（ECB 模式）**（使用 `gmssl` 库）
- **RC6 加密**（包括密钥扩展、加密与解密）
- **哈希函数**：
  - SHA-1
  - SHA-256
  - SHA3-256
  - RIPEMD-160
- **HMAC 摘要**：
  - HMAC-SHA1
  - HMAC-SHA256
- **PBKDF2 密码哈希扩展**（基于 SHA256）

---

## 🚀 使用方法

### 📋 环境依赖

请确保已安装以下 Python 库：

```bash
pip install pycryptodome gmssl
