import os


class Account:
    def __init__(self, account_number, pin, balance=0):
        self.account_number = account_number
        self.pin = pin
        self.balance = balance
        self.transaction_history = []

    def deposit(self, amount):
        if amount > 0:
            self.balance += amount
            self.transaction_history.append(f"存款: +{amount} 元，当前余额: {self.balance} 元")
            return True
        return False
    
    def withdraw(self, amount):
        if amount <= 0:
            self.transaction_history.append(f"无效取款金额: {amount} 元")
            return False
        if amount > self.balance:
            self.transaction_history.append(f"取款失败: 余额不足，尝试取款 {amount} 元，当前余额: {self.balance} 元")
            return False
        self.balance -= amount
        self.transaction_history.append(f"取款: -{amount} 元，当前余额: {self.balance} 元")
        return True

    def transfer(self, target_account, amount):
        if amount <= 0:
            self.transaction_history.append(f"⚠️ 无效转账金额: {amount} 元")
            return False
        if self.withdraw(amount):
            target_account.deposit(amount)
            self.transaction_history.append(
                f"转账到 {target_account.account_number}: -{amount} 元，当前余额: {self.balance} 元")
            target_account.transaction_history.append(
                f"从 {self.account_number} 转入: +{amount} 元，当前余额: {target_account.balance} 元")
            return True
        return False


class Bank:
    def __init__(self):
        self.accounts = {}
        self.load_accounts()

    def load_accounts(self):
        if os.path.exists('accounts.txt'):
            try:
                with open('accounts.txt', 'r') as file:
                    for line in file:
                        parts = line.strip().split(',')
                        account_number = parts[0]
                        pin = parts[1]
                        balance = float(parts[2])
                        account = Account(account_number, pin, balance)
                        for i in range(3, len(parts)):
                            account.transaction_history.append(parts[i])
                        self.accounts[account_number] = account
            except Exception as e:
                print(f"加载账户数据时出错: {e}")

    def save_accounts(self):
        try:
            with open('accounts.txt', 'w') as file:
                for account in self.accounts.values():
                    line = f"{account.account_number},{account.pin},{account.balance},"
                    line += ','.join(account.transaction_history)
                    file.write(line + '\n')
        except Exception as e:
            print(f"保存账户数据时出错: {e}")

    def register(self, account_number, pin):
        if account_number in self.accounts:
            print("该账号已存在，请选择其他账号。")
            return False
        new_account = Account(account_number, pin)
        self.accounts[account_number] = new_account
        self.save_accounts()
        print("注册成功！")
        return True

    def login(self, account_number, pin):
        if account_number in self.accounts and self.accounts[account_number].pin == pin:
            print("登录成功！")
            return self.accounts[account_number]
        print("账号或密码错误，请重试。")
        return None

    def get_account(self, account_number):
        return self.accounts.get(account_number)


def main():
    bank = Bank()
    current_account = None

    while True:
        if current_account is None:
            print("\n欢迎使用银行管理系统")
            print("1. 注册")
            print("2. 登录")
            print("3. 退出")
            choice = input("请输入你的选择: ")

            if choice == '1':
                account_number = input("请输入要注册的账号: ")
                pin = input("请输入密码: ")
                bank.register(account_number, pin)
            elif choice == '2':
                account_number = input("请输入账号: ")
                pin = input("请输入密码: ")
                current_account = bank.login(account_number, pin)
            elif choice == '3':
                print("感谢使用，再见！")
                break
            else:
                print("无效的选择，请重新输入。")
        else:
            print("\n账户操作菜单")
            print("1. 存款")
            print("2. 取款")
            print("3. 转账")
            print("4. 查看交易记录")
            print("5. 退出登录")
            sub_choice = input("请输入你的选择: ")

            if sub_choice == '1':
                amount = float(input("请输入存款金额: "))
                if current_account.deposit(amount):
                    print("存款成功！")
                    bank.save_accounts()
                else:
                    print("存款金额必须大于 0，请重新输入。")
            elif sub_choice == '2':
                amount = float(input("请输入取款金额: "))
                if current_account.withdraw(amount):
                    print("取款成功！")
                    bank.save_accounts()
                else:
                    print("取款失败，可能是余额不足或输入金额无效。")
            elif sub_choice == '3':
                target_account_number = input("请输入目标账号: ")
                target_account = bank.get_account(target_account_number)
                if target_account:
                    amount = float(input("请输入转账金额: "))
                    if current_account.transfer(target_account, amount):
                        print("转账成功！")
                        bank.save_accounts()
                    else:
                        print("转账失败，可能是余额不足或输入金额无效。")
                else:
                    print("目标账号不存在，请重新输入。")
            elif sub_choice == '4':
                print("交易记录:")
                for record in current_account.transaction_history:
                    print(record)
            elif sub_choice == '5':
                current_account = None
                print("已退出登录。")
            else:
                print("无效的选择，请重新输入。")


if __name__ == "__main__":
    main()
    
    