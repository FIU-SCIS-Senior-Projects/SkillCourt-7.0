function MyAccountRedirect() {
    var user = firebase.auth().currentUser;
    console.log(user);
    if (user) {
        console.log(user);
        window.location.replace("/users");
    } else {
        window.location.replace("/signin");
    }
}

function openNav() {
    document.getElementById("sidenav").style.width = "250px";
    document.body.style.backgroundColor = "rgba(0,0,0,0.4)";
}

function closeNav() {
    document.getElementById("sidenav").style.width = "0";
    document.body.style.backgroundColor = "#ccc"

}
