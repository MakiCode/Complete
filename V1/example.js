/**
 * Created by Huulktya on 4/7/14.
 */

function ClassName(ConstructerArg, OptionalArg) {

    var privateField = 1;

    function privateMethod() {
        return 1;
    }

    var optionalArg = OptionalArg ? OptionalArg : "Default Value";

    this.constructor.staticField++;

    this.publicMethod = function () {
        return privateField;
    };

    this.publicField = 1;
}

ClassName.prototype.PublicReadAndWriteMethod = function () {
    //Calls the class of the subclass's constructor.
    var someClass = new this.constructor();
};
ClassName.prototype.PublicReadAndWriteField = 1;

ClassName.PublicReadWriteStaticField = 1;

function SubOfClassName() {

}
//The point where inheritance takes place
SubOfClassName.inheritsFrom(ClassName);

// Overriding a super class method
SubOfClassName.prototype.PublicReadAndWriteMethod = function() {

    //Calling a superclass method
    this.parent.PublicReadAndWriteMethod.call(this);
};