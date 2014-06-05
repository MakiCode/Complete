/**
 * Created by Huulktya on 4/8/14.
 */
Game.Preloader = function () {

};

//Load as mant assets as needed
Game.Preloader.prototype = {
    baseEntityWidth: 100,
    baseEntityHeight: 200,
    preload: function () {
        //Load Assets
        //Load any and all art
        this.load.image('background', 'assets/background.png');
        this.load.image('blue', 'assets/blue-ally.png');
        this.load.image('blue-shot', 'assets/blue-shot.png');
        this.load.image('blue-card', 'assets/blue-card.png');
        this.load.image('cyan', 'assets/cyan-ally.png');
        this.load.image('cyan-shot', 'assets/cyan-shot.png');
        this.load.image('cyan-card', 'assets/cyan-card.png');
        this.load.image('ui-background', 'assets/pallete-background.png');
        this.load.image('field', 'assets/playing-field.png');
        this.load.image('marker', 'assets/marker.png');
        this.load.image('cap', 'assets/cap.png');
        this.load.image("enemy1", "assets/enemy1-spritesheet.png");
        this.load.image("enemy2", "assets/enemy2-spritesheet.png");
        this.load.image("resource", "assets/resource.png");
        this.load.image("resource-background", "assets/resource-background.png");
        this.load.image("resource-textbar", "assets/resources-textbar.png");
        this.load.image("health-area", "assets/health-area.png");
        this.load.image("win", "assets/win.png");
        this.load.image("lose", "assets/lose.png");
    },

    create: function () {
        //All assets have been loaded, set the loading bar state to "almost complete" while we decode sounds
    },

    update: function () {

        //If all assets are loaded + decoded, switch states to menu screen!
        //game.state.start("MainMenu");
        this.state.start("Level1");
    }

};