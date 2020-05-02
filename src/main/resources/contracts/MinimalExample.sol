pragma solidity ^0.4.0;
contract Minimal{

    uint  c;
    
    function add(uint a, uint b) public {
        c = a +b;
    }
    
    function getC() view public returns (uint) {
        return c;
    }
    
}
    