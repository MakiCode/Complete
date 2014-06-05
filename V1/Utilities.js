/**
 * Created by Huulktya on 4/7/14.
 */

/**
 * Taken from this tutorial: http://phrogz.net/JS/classes/OOPinJS2.html
 * @param parentClassOrObject The super class of this class
 * @returns {Function} Unknown/unimportant
 */
Function.prototype.inheritsFrom = function( parentClassOrObject ){
    if ( parentClassOrObject.constructor == Function )
    {
        //Normal Inheritance
        this.prototype = new parentClassOrObject;
        this.prototype.constructor = this;
        this.prototype.parent = parentClassOrObject.prototype;
    }
    else
    {
        //Pure Virtual Inheritance
        this.prototype = parentClassOrObject;
        this.prototype.constructor = this;
        this.prototype.parent = parentClassOrObject;
    }
    return this;
}