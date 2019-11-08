import socket
import sys
import string
import random
import time

def randomString(stringLength=10):
    letters = string.printable
    return ''.join(random.choice(letters) for i in range(stringLength))

def randomString(stringLength=10):
    letters = string.ascii_lowercase
    return ''.join(random.choice(letters) for i in range(stringLength))

# Create a TCP/IP socket

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect(("localhost", 4446))
sock.sendall(bytes(input("What to send"),"UTF-8"))
sock.close()

if(input("send random data?") == "yes"):
    for i in range(1,20):
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect(("localhost", 4446))
        sock.sendall(bytes(randomString(500),"UTF-8"))
        print("Sent")
        sock.close()

if(input("send random messages?") == "yes"):
    for i in range(1,20):
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect(("localhost", 4446))
        sock.sendall(bytes("msg" + randomString(500),"UTF-8"))
        print("Sent")
        sock.close()

if(input("send random messages readable?") == "yes"):
    for i in range(1,20):
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect(("localhost", 4446))
        sock.sendall(bytes(randomString(500),"UTF-8"))
        print("Sent")
        sock.close()
i = input("open bunches o hunches of sockets?")
for i in range (0,int(i)):
    so = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    so.connect(("localhost", 4446))

#what if we do a sslow loooris?    
