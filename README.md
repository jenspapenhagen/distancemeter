# This is a MircoService for Calculation the Distance in km form one german zipCode to another zipCode

#### (Getting the Location of one ZipCode)

This Project is based on a default Quarkus Project.
And use the Rest-APi from https://github.com/jenspapenhagen/zipcode/

## Main Goal:
- playing around with Quarkus Extensions
- fun


Form the lowest zipCode from Hamburg to lowest zipCode in berlin 
```
wget --header="Content-Type: text/json" http://localhost:8080/20095/10115/
```
For getting this Response
```json
{
  "plz1":20095,
  "plz1":10115,
  "distance": 255.65  
}
```

Testing:

