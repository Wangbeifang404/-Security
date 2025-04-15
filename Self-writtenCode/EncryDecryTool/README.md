项目概述
本后端系统基于SpringBoot框架与BouncyCastle加密套件，实现了包含MD5在内的多类密码学算法支持。项目采用分层架构设计，核心功能通过Controller层接收请求，Service层实现加密算法调度与业务逻辑处理。

技术栈特性
加密算法支持
​​对称加密​​
AES | SM4 | RC6
​​哈希算法​​
MD5 | SHA1/SHA256/SHA3 | HMAC系列 | PBKDF2
​​公钥体系​​
RSA-1024 | ECC-160 | ECDSA（证书签名验证）
​​编码转换​​
Base64编解码 | UTF-8字符集支持