import socket
import time
import sys

server = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
server.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
# indefinitely when trying to receive data.
server.settimeout(0.2)
server.bind(("", 4444))
message = bytes(sys.argv[1],"UTF-8")
server.sendto(message, ('<broadcast>', 4445))
print("message(s) sent!")
