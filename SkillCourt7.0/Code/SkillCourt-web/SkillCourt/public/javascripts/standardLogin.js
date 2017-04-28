function login() {
    var success = false;
    var emailAddress = document.getElementById('user').value;
    var password = document.getElementById('pass').value;
    if (!emailAddress && !password) {
        alert("Please fill out the form before submitting.");
    } else if (!emailAddress && password) {
        alert("Please enter your E-mail Address before submitting.");
    } else if (emailAddress && !password) {
        alert("Please enter your Password before submitting.");
    } else if (!emailAddress.includes("@") || !emailAddress.includes(".")) {
        alert("Please enter a valid e-mail address");
    } else {
        firebase.auth().signInWithEmailAndPassword(emailAddress, password).then(function (result) {
            // 'then' function if needed in future
        }).catch(function (error) {
            var errorCode = error.code;
            var errorMessage = error.message;
            //console.log(errorCode);
            //console.log(errorMessage);
        });
        var user = firebase.auth().currentUser;
        console.log(user);
        if (user) {
            sessionStorage.loggedIn = true;
            console.log('login successful');
            console.log(user);
            MyAccountRedirect();
        } else {
            sessionStorage.loggedIn = false;
            console.log('login failed');
            alert('login unsuccessful. Please check your credentials and try again.');
        }
    }
}

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