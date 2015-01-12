##CQL

* (+) Error reporting is not bad at all, contains hints how to resolve errors.
* (-) Not very convenient - JPA does a much better job with ORM, although it is not as flexible as cassandra rows. Currently, there seem to be at least 1 or 2 Cassandra object mapping wrappers under development.
* (-) There doesn't seem to be a nice way of referencing rows with compound keys (not client-defined) that were just inserted into DB.

##CLI

* (+) Basic usage is more concise (less code).
* (+) Usable from pretty much anywhere without additional environments like Java.
* (-) Deprecated in newer releases.
* (-) Not suited for transactional behaviour and performance.
* (-) Versions of client and server binary distributions may differ.

CLI is obviously a good choice for small admin tasks only - client applications should be written in CQL, which should be more refined in future with support for object mapping.