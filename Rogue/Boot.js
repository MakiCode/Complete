//Set basic settings and load assets fro preloader (loading bar, etc.)
Game = {};
/**
 * Created by Huulktya on 4/8/14.
 */

Game.Boot = function () {

};

Game.Boot.prototype = {
    preload: function () {
        //Load any assets needed for loading bar
    },
    create: function () {
        //Set settings
        //Go to preloader
        this.state.start("Preloader");
    }
};

