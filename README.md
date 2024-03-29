Challenge is very nice, its a good example for improve and show object oriented design, clean code, SOLID and testing/TDD.
There is a short explanation of my decisions and thoughts about the code.

PD.
I've left opened a PR from develop to master that we should merge to update to java 11 and spring boot 2.1, there is no so manny changes in code, just local variables type inference (var)

# 📝 Test

End to end tests provided by the legacy team are awesome! I've just added a missing one: an user who check send a request
to him/herself.
Anyway, I've thought in all the edge cases and applied TDD with unit tests (using junit library) so all code is tested. 
Dependencies are mocked with mockito library.

# ⚙️ Configuration

Here we have two different classes with same purpose

Router: I decided to save endpoints and param names in a router class, I think is easier if some day
        we want to change one of them.

Conf: For beans injections.

# 🕹 Controller

Controller was the past legacy package, there we have endpoints and just call to each service.
If everything is ok, they will response with an OK code.

# ⚖️ Service

They have the business logic, they ask to repository about users, passwords and lists and they check
inputs and throw exceptions when a resource wont exist (not found) or when users will do something
incorrect (bad request) also they will save if everything is okay.

# ⛔️ Exceptions

I dont want to throw existing exceptions so I've decided to have a couple of custom exceptions, they
extend runtime exception so I can throw them whenever.
- BadRequestException for wrong inputs
- NotFoundExceptions for not existing resources

# 🤲 Handler

They listen for custom exceptions and they will response with the http code and a explanation of the error.

# 📸 Model

I've decided to split passwords from users, Im not sure if this is the correct way but I think for friends
relationships we dont need passwords and it would be a security problem, so:

User and Password will throw an exception if input is different than the explanations of the problem.
Also, I've decided add a hash (md5) for passwords, a quick solution for not handle in clear sensitive data.

# 💾 Repository

I decided to implement three different interfaces in a memory class. 

I have not considered inconsistency in the bd, but if a user who exists (after check) has no an entry in any of
the maps, repository in memory will return an empty set.

* UsersRepository 
    For the users and passwords, it will tell you if a user exists and will return its password.
    
* FriendRepository & RequestRepository
    For the friends requests and friend lists, they will return and save the lists.
    I decided keep the relations duplicated, I mean, friend A in B list and friend B in A list. Other valid option would
    be have a relationship object composed of two users and keep in a single set this objects but I think this way
    will increase the complexion when someone wants to list friends… (You have to iterate all set looking for object relation
    which contains the user).

* MemoryRepository implements this three interfaces and keep objects in memory.
        I decided to use concurrent hash maps with user as key and sets for the lists.
        I'd prefer to use just sets for the friend list but one of the tests that legacy team wrote
        told me that friend list has order.
