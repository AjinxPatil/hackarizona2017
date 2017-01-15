import json
import user_account

def ToJsonString(ipobject):
    return json.dumps(ipobject.__dict__);

if __name__ == '__main__':
    user = user_account.user_accounts('abc', 'username', 'password', '09988273')
    print ToJsonString(user)


