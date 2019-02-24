I've applied MVC as pattern for design the API and TDD for develop,
so there is a short explanation of my decisions and thinks about the code.


⚙️ Configuration
Here we have two different classes with same purpose

Router: I decided to save endpoints and param names in a router class, I think is easier if some day
we want to change one of them.
Conf: For beans injections.

🕹 Controller/legacy

I didnt change this package because legacy, there we have endpoints and just call to each service.
If everything is ok, they will response with an OK code.

⚖️ Service

They have the business logic, they ask to repository about users, passwords and lists and they check
inputs and throw exceptions when a resource wont exist (not found) or when users will do something
incorrect (bad request) also they will save if everything is okay.

⛔️ Exceptions

I dont want to throw existing exceptions so I've decided to have a couple of custom exceptions, they
extend runtime exception so I can throw them whenever.
BadRequestException for wrong inputs
NotFoundExceptions for not existing resources

🤲 Handler

They listen for custom exceptions and they will response with the http code and a explanation of the error.

📸 Model

I've decided to split passwords from users, Im not sure if this is the correct way but I think for friends
relationships we dont need passwords and it would be a security problem, so:

User and Password will throw an exception if input is different than the explanations of the problem.
Also, I've decided add a hash (md5) for passwords, a quick solution for not handle in clear sensitive data.

💾 Repository

I decided to implement three different interfaces in a memory class.

UsersRepository for the users and passwords, it will tell you if a user exists and will return its password.
FriendRepository for the friends relations, it will return and save the friend list.
RequestRepository for the friends requests, it will return and save the request list.
MemoryRepository implements this interfaces and keep objects in memory.
    - About linkedHashSet: 
        I decided to use concurrent hash maps with user as key and sets for the lists.
        I'd prefer to use sets for the friend list but one of the tests that legacy team wrote
        told me that friend list has order.
    


