var heading_App = document.getElementById("cycleAppHead");
var text_App = document.getElementById("cycleAppBody");
var screenshot_App = document.getElementById("cycleAppImage");

var currentlyDisplayed_App = 0;

var headings_App = [
    "Hone Your Skills",
    "Go Head to Head with Friends",
    "Find Opponents Around the World"];

var texts_App = [
    "Track your progress from game to game as SkillCourt logs your statistics " +
    "in real time." +
    "<br><br>" +
    "In our standard gameplay modes both correct and incorrect hits will be recorded " +
    "as they occur, allowing onlookers to monitor your score",

    "Challenge your friends with our multiplayer function. Friends can go " + 
    "head to head with one another with the same sequences. Compare your " + 
    "scores and keep an ongoing record of wins and losses",

    "Whether for practice or competition you can browse online users and " +
    "challenge them to matches at any time of the day" + 
    "<br><br>" + 
    "Once you find an opponent that poses a challenge, use your match history " +
    "to set up rematches"
];

var screenshot1 = document.createElement("IMG");
screenshot1.setAttribute("src", "/images/AppScreen_Menu.png");
var screenshot2 = document.createElement("IMG");
screenshot2.setAttribute("src", "/images/AppScreen_Multiplayer.png");
var screenshot3 = document.createElement("IMG");
screenshot3.setAttribute("src", "/images/AppScreen_PlayerList.png");

var screenshots = [screenshot1, screenshot2, screenshot3];


function previous_App() {
    currentlyDisplayed_App = (currentlyDisplayed_App + headings_App.length - 1) % headings_App.length;
    displayCurrentInfo_App();
}


function next_App() {
    currentlyDisplayed_App = (currentlyDisplayed_App + 1) % headings_App.length;
    displayCurrentInfo_App();
}


function displayCurrentInfo_App() {
    heading_App.innerHTML = headings_App[currentlyDisplayed_App];
    text_App.innerHTML = texts_App[currentlyDisplayed_App];
    screenshot_App.setAttribute("src", screenshots[currentlyDisplayed_App].getAttribute("src"));
}




//----------------------------------------------------------------------------


var heading_Pad = document.getElementById("cyclePadHead");
var text_Pad = document.getElementById("cyclePadBody");
var screenshot_Pad = document.getElementById("cyclePadImage");

var currentlyDisplayed_Pad = 0;

var headings_Pad = [
    "Visible Indoors and Out",
    "Sturdy",
    "Portable"
];

var texts_Pad = [
    "The LED lighting used in the Pads allows them to be easily seen in all " + 
    "environments including gyms, backyards, and indoor/outdoor fields.",

    "The materials used in the construction of the SkillCourt Pad allows it to " +
    "absorb even the most powerful impacts from a soccer ball." + 
    "<br><br>" + 
    "Whether you're doing a speed drill with light taps or testing your " + 
    "strength, the SkillCourt Pad will hold up all day long.",

    "The lightweight build of the SkillCourt Pad allows it to easily be " + 
    "transported to and from event locations with ease."

];

var PadImage1 = document.createElement("IMG");
PadImage1.setAttribute("src", "/images/Pad_LED.jpg");
var PadImage2 = document.createElement("IMG");
PadImage2.setAttribute("src", "/images/Pad_ForceChart.jpg");
var PadImage3 = document.createElement("IMG");
PadImage3.setAttribute("src", "/images/screenshot.png");

var PadImages = [PadImage1, PadImage2, PadImage3];

function previous_Pad() {
    currentlyDisplayed_Pad = (currentlyDisplayed_Pad + headings_Pad.length - 1) % headings_Pad.length;
    displayCurrentInfo_Pad();
}


function next_Pad() {
    currentlyDisplayed_Pad = (currentlyDisplayed_Pad + 1) % headings_Pad.length;
    displayCurrentInfo_Pad();
}


function displayCurrentInfo_Pad() {
    heading_Pad.innerHTML = headings_Pad[currentlyDisplayed_Pad];
    text_Pad.innerHTML = texts_Pad[currentlyDisplayed_Pad];
    screenshot_Pad.setAttribute("src", PadImages[currentlyDisplayed_Pad].getAttribute("src"));
}