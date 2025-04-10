from Crypto.Cipher import AES
from Crypto.Util.Padding import pad, unpad
import struct
import math

### ===== AES 加解密 =====
def aes_encrypt(key, data):
    cipher = AES.new(key, AES.MODE_CBC)
    ct_bytes = cipher.encrypt(pad(data, AES.block_size))
    return cipher.iv + ct_bytes

def aes_decrypt(key, enc_data):
    iv = enc_data[:16]
    ct = enc_data[16:]
    cipher = AES.new(key, AES.MODE_CBC, iv)
    return unpad(cipher.decrypt(ct), AES.block_size)
### ===== SM4 加解密 =====

from gmssl.sm4 import CryptSM4, SM4_ENCRYPT, SM4_DECRYPT

def sm4_encrypt(key, data):
    crypt_sm4 = CryptSM4()
    crypt_sm4.set_key(key, SM4_ENCRYPT)
    return crypt_sm4.crypt_ecb(data)

def sm4_decrypt(key, encrypted_data):
    crypt_sm4 = CryptSM4()
    crypt_sm4.set_key(key, SM4_DECRYPT)
    return crypt_sm4.crypt_ecb(encrypted_data)

### ===== RC6 密钥扩展与加解密 =====
def rc6_key_schedule(key, w=32, r=20):
    b = len(key)
    u = w // 8
    c = max(1, math.ceil(b / u))
    L = [0] * c
    for i in range(b-1, -1, -1):
        L[i//u] = (L[i//u] << 8) + key[i]
    Pw = 0xB7E15163
    Qw = 0x9E3779B9
    S = [(Pw + i * Qw) & 0xFFFFFFFF for i in range(2 * r + 4)]
    A = B = i = j = 0
    v = 3 * max(c, 2 * r + 4)
    for _ in range(v):
        A = S[i] = ((S[i] + A + B) << 3 | (S[i] + A + B) >> (w - 3)) & 0xFFFFFFFF
        B = L[j] = ((L[j] + A + B) << ((A + B) & 0x1F) | (L[j] + A + B) >> (w - ((A + B) & 0x1F))) & 0xFFFFFFFF
        i = (i + 1) % (2 * r + 4)
        j = (j + 1) % c
    return S

def rc6_encrypt_block(pt, S, w=32, r=20):
    A, B, C, D = struct.unpack('<4L', pt)
    B = (B + S[0]) & 0xFFFFFFFF
    D = (D + S[1]) & 0xFFFFFFFF
    for i in range(1, r + 1):
        t = (B * (2 * B + 1)) & 0xFFFFFFFF
        t = ((t << 5) | (t >> 27)) & 0xFFFFFFFF
        t %= w
        u = (D * (2 * D + 1)) & 0xFFFFFFFF
        u = ((u << 5) | (u >> 27)) & 0xFFFFFFFF
        u %= w
        A = ((A ^ t) << u | (A ^ t) >> (w - u)) + S[2 * i]
        A &= 0xFFFFFFFF
        C = ((C ^ u) << t | (C ^ u) >> (w - t)) + S[2 * i + 1]
        C &= 0xFFFFFFFF
        A, B, C, D = B, C, D, A
    A = (A + S[2 * r + 2]) & 0xFFFFFFFF
    C = (C + S[2 * r + 3]) & 0xFFFFFFFF
    return struct.pack('<4L', A, B, C, D)

def rc6_decrypt_block(ct, S, w=32, r=20):
    A, B, C, D = struct.unpack('<4L', ct)
    C = (C - S[2 * r + 3]) & 0xFFFFFFFF
    A = (A - S[2 * r + 2]) & 0xFFFFFFFF
    for i in range(r, 0, -1):
        A, B, C, D = D, A, B, C
        u = (D * (2 * D + 1)) & 0xFFFFFFFF
        u = ((u << 5) | (u >> 27)) & 0xFFFFFFFF
        u %= w
        t = (B * (2 * B + 1)) & 0xFFFFFFFF
        t = ((t << 5) | (t >> 27)) & 0xFFFFFFFF
        t %= w
        C = (((C - S[2 * i + 1]) >> t | (C - S[2 * i + 1]) << (w - t)) ^ u) & 0xFFFFFFFF
        A = (((A - S[2 * i]) >> u | (A - S[2 * i]) << (w - u)) ^ t) & 0xFFFFFFFF
    B = (B - S[0]) & 0xFFFFFFFF
    D = (D - S[1]) & 0xFFFFFFFF
    return struct.pack('<4L', A, B, C, D)
from hashlib import sha1, sha256, sha3_256, pbkdf2_hmac
from Crypto.Hash import RIPEMD160, HMAC, SHA1, SHA256

def hash_sha1(data):
    return sha1(data).hexdigest()

def hash_sha256(data):
    return sha256(data).hexdigest()

def hash_sha3(data):
    return sha3_256(data).hexdigest()

def hash_ripemd160(data):
    h = RIPEMD160.new()
    h.update(data)
    return h.hexdigest()

def hmac_sha1(key, data):
    h = HMAC.new(key, digestmod=SHA1)
    h.update(data)
    return h.hexdigest()

def hmac_sha256(key, data):
    h = HMAC.new(key, digestmod=SHA256)
    h.update(data)
    return h.hexdigest()

def hash_pbkdf2(password, salt, iterations=100000):
    key = pbkdf2_hmac('sha256', password, salt, iterations)
    return key.hex()
sample_data = b"HelloWorld123456"
sample_key = b"thisisakey123456"
sample_salt = b"salt1234"

# AES
aes_enc = aes_encrypt(sample_key, sample_data)
aes_dec = aes_decrypt(sample_key, aes_enc)

# RC6（注意必须是16字节输入）
rc6_schedule = rc6_key_schedule(sample_key)
rc6_input = b"abcdefgh12345678"
rc6_enc = rc6_encrypt_block(rc6_input, rc6_schedule)
rc6_dec = rc6_decrypt_block(rc6_enc, rc6_schedule)

# 哈希
print("SHA1:", hash_sha1(sample_data))
print("SHA256:", hash_sha256(sample_data))
print("SHA3:", hash_sha3(sample_data))
print("RIPEMD160:", hash_ripemd160(sample_data))

# HMAC & PBKDF2
print("HMAC-SHA1:", hmac_sha1(sample_key, sample_data))
print("HMAC-SHA256:", hmac_sha256(sample_key, sample_data))
print("PBKDF2:", hash_pbkdf2(sample_key, sample_salt))

# 显示 AES / RC6 结果
print("AES Decrypted:", aes_dec)
print("RC6 Decrypted:", rc6_dec)
from Crypto.Util.Padding import pad, unpad

# 示例数据
sm4_key = b"thisisakey123456"        # 16字节密钥
sm4_data = b"HelloWorld123456"       # 原始数据（16字节）
padded_data = pad(sm4_data, 16)

# 加解密
sm4_enc = sm4_encrypt(sm4_key, padded_data)
sm4_dec = unpad(sm4_decrypt(sm4_key, sm4_enc), 16)

print("SM4 Encrypted:", sm4_enc)
print("SM4 Decrypted:", sm4_dec)
