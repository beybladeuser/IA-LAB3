# IA-LAB3
Blocks world problem with 7 blocks using A* and IDA*

The A* Branch has the A* implementation of this proplem. To use this algorithm implementation one must implement the Ilayout class, so that one can create a AStar<myClass> class. This is slower than IDA*.

The IDA* Branch has the IDA* implementation of this proplem. To use this algorithm implementation one must implement the Ilayout class, so that one can create a IDAStar<myClass> class. This is after a certain deph becomes really slow.

Both A* and IDA* share the Table class which implements from Ilayout and represents the table and the blocks as described in the Blocks world problem.
The inputs are 2 strings in the following format: a chain of letters like ABC is a stack, stacks are chains of letters separated by a space like: ABC GE, where ABC is a stack and C is at the top and GE is another stack in the same table. The first string is the inicial state and the 2nd is the goal/final state.
2 Table objects are considered equal if the order of the blocks in a stack are equal, and the order of the stacks dont matter, i.e. ABC ED == ED ABC


THE BLOCK LIMIT OF THE Table IS SET TO 7 BUT CAN BE CHANGED BY GOING TO THE Table CLASS AN CHANGING THE VALUE maxDim TO THE DESIRED VALUE.
