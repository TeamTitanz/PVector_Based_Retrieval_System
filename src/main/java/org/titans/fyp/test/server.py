import socket

HOST = "localhost"
PORT = 8888
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print('Server created')

try:
    s.bind((HOST, PORT))
except socket.error as err:
    print('Bind failed. Error Code : ' .format(err))
s.listen(10)
print("Server Listening")
conn, addr = s.accept()
while(True):
    #print("Message sent")
    data = conn.recv(20240)
    print(data.decode())
    conn.send(bytes("Python Send"+"\r\n"))
    #break
    
