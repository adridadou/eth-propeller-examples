pragma solidity ^0.4.25;

contract myFirstContract {

    uint someNumber;

    function getString() pure public returns(string) {
        return "hello world";
    }

    function setNumber(uint i) public {
        someNumber = i;
    }

    function getNumber() view public returns(uint) {
        return someNumber;
    }

}
