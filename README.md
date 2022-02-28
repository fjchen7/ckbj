ckbj is proof of concept (still in work-in-process) of CKB SDK for JAVA. 

ckbj's responsibility

- fiexible and easy construction of new transaction.
- transaction signing abstraction.
- extensibility for signing method and other operations of new script.
- configuration support for different network.

However, under this idea ckbj needs a powerful service to query data on CKB chain. This service should be able to provide an efficient and flexible data querying ability. The biggest benefit of such layer structure is decoupling: data querying service does not need to care about any business logic and should only focus on providing stable data-querying APIs with good performance, while ckbj is mainly in charge of all user-oriented interfaces including construcing, sigining and sending transaction, to meet ever-changing business requirement.

