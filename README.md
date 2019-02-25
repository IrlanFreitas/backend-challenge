I've applied MVC as pattern for design the API and TDD for develop the code.
There is a short explanation of my decisions and thoughts about the code.

# 📝 Test

End to end tests provided by the legacy team are awesome! I've just add a missing one: check send a request to himself
Anyway, I've use TDD so now we have unit tests (using junit library) for every method and other classes mocked, so 
corner cases are included.

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
BadRequestException for wrong inputs
NotFoundExceptions for not existing resources

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




# Schibsted Spain Backend Coding Challenge

Welcome to our coding challenge, hope you enjoy it!

We need your help migrating a legacy social network service.

This service have users that can friend other users and offers an HTTP API to do so.

## Requirements

The use cases that need to be implemented are:

* Sign up
  * A new user requests to register to our service, providing its username and password.
  * Username must be unique, from 5 to 10 alphanumeric characters.
  * Password from 8 to 12 alphanumeric characters.
* Request friendship
  * A registered user requests friendship to another registered user.
  * A user cannot request friendship to himself or to a user that already has a pending request from him.
* Accept friendship
  * A registered user accepts a requested friendship.
  * Once accepted both users become friends forever and cannot request friendship again.
* Decline friendship
  * A registered user declines a requested friendship.
  * Once declined friendship can be requested again.
* Friends
  * List friends of a registered user.

There is one drawback, we have to maintain the API of the legacy service, which is awful but nothing we can do 😞.
We hope we can refactor the API in a future iteration, **but not in this challenge**.

So we provide you with an initial implementation of controllers under package `com.schibsted.spain.friends.legacy` that fulfill the legacy API and you can work from there.

The legacy team has provided us with a script that you can execute to check if your implementation is on the right path.
Just execute `bash -c scripts/legacy-test` while your service is running and expect all checks to pass.
This script uses `bash` and should work just fine in macOs and linux. In windows you can use [git-bash](https://gitforwindows.org/).

No database needed, you can persist everything in memory.

We expect from you to apply good practices and be proud of what you do. Good luck!

## Build & Run

`./gradlew bootRun`
