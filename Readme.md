#Instant Messaging
##How it works
The server is working as a Thread-Pool TCP Server. The client may connect and the traffic is 
going as follows:
- first send 4 bytes which represent an integer that stores the total bytes of the following 
package.
- then the amount of bytes is send and received

What the bytes represent is specified below

##The Protocol

- 1 byte - packageType
    - registration
    - authentication
    - status
    - message
    - acknowledgement
    
###Registration

1. Client registration request with userID (e.g. phone number)
2. Server response
    - no if uID is already registered / uId is not valid
    - yes, with pwd
3. Client confirms pwd
4. _`Server acknowledges registration`_

###Authentication

1. Client authentication request with uID
2. Server response
    - no if uID doesn't exist / _`is already online`_
3. Client sends pwd
5. Server response
    - no if pwd is wrong
    - yes, with connectionID, marks user online