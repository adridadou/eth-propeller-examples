pragma solidity ^0.4.6;


contract myContract2 {
    string i1;

    string i2;

    address owner;

    constructor() public {
        owner = msg.sender;
    }

    function myMethod(string value) public returns (uint) {
        i1 = value;
        return 12;
    }

    function myMethod2(string value) public returns (bool success) {
        i2 = value;
        return true;
    }

    function myMethod3(string value) public payable returns (bool success) {
        i2 = value;
        return true;
    }

    function getEnumValue() pure public returns (uint) {return 1;}

    function getI1() constant public returns (string) {return i1;}

    function getI2() constant public returns (string) {return i2;}

    function getT() pure public returns (bool) {return true;}

    function getM() pure public returns (bool, string, uint) {return (true, "hello", 34);}

    function getOwner() view public returns (address) {return owner;}

    function getArray() pure public returns (uint[10] arr) {
        for (uint i = 0; i < 10; i++) {
            arr[i] = i;
        }
    }

    function getSet() public pure returns (uint[10] arr) {
        for (uint i = 0; i < 10; i++) {
            arr[i] = i;
        }
    }

    function throwMe() public pure {
        revert();
    }

    function getInitTime(uint time) public pure returns (uint) {
        return time;
    }

    function getAccountAddress(address addr) public pure returns (address) {
        return addr;
    }
}
