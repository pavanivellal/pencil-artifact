# Shopping cart

A shopping cart application that returns the total value of all items in the shopping cart by applying applicable volume based and special offers. 

## Installation

Download from https://github.com/pavanivellal/pencil-artifact.git

Create Jarfile - run `lein uberjar`

## Usage

    $ java -jar pencil-artifact-0.0.1.jar cart-items-with-qty-string offer-date
    -> offer-date : yyyy-MM-dd format or NA if not applicable
        ex : 2020-10-01
    -> cart-items-with-qty-string : Enter item name, followed by "x" and quantity if greater than 1
        ex : "Cookie x 8, Cheesecake x 4"
    

## Examples
If running from clojure REPL, go to namespace pencil-artifact.core and execute below examples: 
```
(-main "Cookie, Brownie x 4, Cheesecake" "NA")
(-main "Cookie x 8" "NA")
(-main "Cookie, Brownie, Cheesecake, Donut x 2" "NA")
(-main "Cookie x 8, Cheesecake x 4" "2021-10-01")
```

If running from command line using jar file, try the following: 
(Ensure resource folder is in the same directory as the jarfile)
Example 1 : 
```
$ java -jar pencil-artifact-0.0.1-standalone.jar "Cookie x 8, Cheesecake x 4" 2020-10-01
Output : 
Offer date :  2020-10-01
Cart items with quantity : 
({:id 3, :qty 8, :name "cookie"} {:id 2, :qty 4, :name "cheesecake"})
Cart total :  24.0
```

Example 2 : 
```
java -jar pencil-artifact-0.0.1-standalone.jar "Cookie, Brownie x 4, Cheesecake" "NA"
Output : 
Offer date :  nil
Cart items with quantity : 
({:id 3, :qty 1, :name "cookie"}
 {:id 1, :qty 4, :name "brownie"}
 {:id 2, :qty 1, :name "cheesecake"})
Cart total :  16.25
```

## Testing
In the terminal run : `lein test`

Parse items from input string test : 
`lein test :items`

Offers helper to get offers for given item and qty tests : 
`lein test :offers`

Price helper to get best price for given item and qty tests :  
`lein test :price`










