from flask import Flask, request

app = Flask(__name__)

@app.route("/", methods=["GET", "POST"])
def hello_world():
    if request.method == 'POST':
        return "<p>POST request successful!</p>"
    else:
        return "<p>Hello, World!</p>"

@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        user_id = request.form.get('user_id', 'Unknown')
        username = request.form.get('username', 'Unknown')
        return f"User ID: {user_id}, User Name: {username}"
    elif request.method == 'GET':
        return '''
            <form method="POST">
                User ID: <input type="text" name="user_id"><br>
                User Name: <input type="text" name="username"><br>
                <input type="submit" value="Login">
            </form>
        '''

if __name__ == '__main__':
    app.run(debug=True)