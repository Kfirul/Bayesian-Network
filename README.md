# Bayesian-Network-Project
## This project is implementing Bayesian Network simple, deduction and Variable Elimination Algorithm.

## Bayesian Network:
A Bayesian network (also known as a Bayes network, Bayes net, belief network, or decision network)
is a probabilistic graphical model that represents a set of variables and their conditional dependencies via a directed acyclic graph (DAG).
Bayesian networks are ideal for taking an event that occurred and predicting the likelihood that any one of several possible known causes was the contributing factor.
For example, a Bayesian network could represent the probabilistic relationships between diseases and symptoms.
Given symptoms, the network can be used to compute the probabilities of the presence of various diseases. (wikipedia)

## Simple Deduction
 In a Bayesian network, we have variables represented as nodes, and these variables can have dependencies on each other. The dependencies are represented by arrows between the nodes.

To make deductions in a Bayesian network, we need to define the probabilities associated with each variable. This includes the probabilities of the variables themselves and the conditional probabilities that describe how one variable depends on another.

When we have observed evidence about certain variables, we can use that evidence to update our probabilities and make deductions about other variables in the network. This is done using Bayes' theorem, which allows us to calculate the probability of a variable given the observed evidence.

Bayes' theorem states that the probability of a variable A given evidence E is proportional to the probability of the evidence E given variable A, multiplied by the prior probability of A, divided by the probability of the evidence E.

In a Bayesian network, we can propagate the observed evidence through the network by updating the probabilities of the variables based on the conditional probabilities and the observed evidence. This allows us to make deductions and calculate the probabilities of other variables given the evidence we have.

By iteratively updating the probabilities based on observed evidence, we can perform deductions and make informed inferences about the variables in the Bayesian network.

## Variable elimination
Variable elimination (VE) is a simple and general exact inference algorithm in probabilistic graphical models, such as Bayesian networks and Markov random fields.[1] It can be used for inference of maximum a posteriori (MAP) state or estimation of conditional or marginal distributions over a subset of variables. The algorithm has exponential time complexity, but could be efficient in practice for the low-treewidth graphs, if the proper elimination order is used. (wikipedia)

It is called Variable Elimination because it eliminates one by one those variables which are irrelevant for the query.

It relies on some basic operations on a class of functions known as factors.
It uses an algorithmic technique called dynamic programming
Variable elimination process:
We would like to compute: P(Q|E1=e1,...,Ek=ek)

Start with initial factors
local CPTs instantiated by evidence
If an instantiated CPT becomes one-valued, discard the factor
While there are still hidden variables (not Q or evidence):
Pick a hidden variable H
Join all factors mentioning H
Eliminate (sum out) H
If the factor becomes one-valued, discard the factor
Join all remaining factors and normalize
there are 3 operations in this process: Join Factors, Eliminate, Normalize
A. JOIN:
Get all factors over the joining variable
Build a new factor over the union of the variables
B. ELIMINATE
Take a factor and sum out a variable - marginalization
Shrinks a factor to a smaller one
C. NORMALIZE
Take every probability in the last factor and divide it with the sum of all probabilities in the factor (including the one we are dividing).
The answer we are looking for is the probability of the query value.
