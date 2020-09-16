# Introduction to pencil-artifact

Application to calculate the total price of shopping cart by applying special offers and volume based offers applicable.

## Application Flow
1. Get list of cart items along with quantity and offer date as input
2. Calculates all offers that apply for a given item, it's quantity and offer date (optional)
3. Returns best price for each item and quantity 
4. Return summation of best prices for all cart items 

## Data structure and Storage 
A no-sql data storage format for this shopping cart application has been chosen. 
For purpose of this simple application, all data has been stored in files and Clojure ATOMs are used to read data from files across the project.

#### Resource folder contains data files 
1. `resources/name.edn` - Item name to item id mapping 
2. `resources/offers.edn` - Offers mapping with key as item id making offer lookup time O(1)
3. `resources/products.edn` - Products data with key as item id making item details lookup time O(1)

## Assumptions made 
0. Regular application and not a web application. Have not made use of the imageURL data. 
1. Customer would be returned best price amongst multiple offers applicable on the product.
2. Higher the quantity, better the offer. 
3. Special offers shall be applied before bulk offers with the assumption that special offers are better than bulk offers.
4. Special offers, if percentage based are on regular item prices.
5. Only 1 best offer applies on an item at a time in a shopping cart.

## Some possible future enhancements
1. Allow multiple offers to be applied on a single item.
2. Convert to web application .

