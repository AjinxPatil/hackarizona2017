class Patient():
    def __init__(self, email, firstname, lastname):
        self.id = email
        self.firstname = firstname
        self.lastname = lastname
        self.phone = ''

    def set_fbtoken(self, token):
        self.fbtoken = token
