


function createAccount() {
    var firstName = document.getElementById('Fname').value;
    var lastName = document.getElementById('Lname').value;
    var email = document.getElementById('email1').value;
    var emailVerify = document.getElementById('email2').value;
    var password = document.getElementById('password1').value;
    var passwordVerify = document.getElementById('password2').value;

    if (!firstName || !lastName || !email || !emailVerify || !password || !passwordVerify) {
        alert("Please complete the entire form before submitting");
    } else if (email !== emailVerify) {
        alert("Emails do not match");
    } else if (password !== passwordVerify) {
        alert("Passwords do not match");
    } else {
        console.log("Acceptable Input");
        firebase.auth().createUserWithEmailAndPassword(email, password).then(function (result) {
            console.log('Account created successfully');
            sessionStorage.loggedIn = true;

            var user = firebase.auth().currentUser;
            sessionStorage.uName = firstName + " " + lastName;
            user.updateProfile({
                displayName: firstName + " " + lastName
            }).then(function () {
                console.log('displayName added successfully');
                console.log(user.displayName);
                writeData();
            }, function (error) {
                console.log(error);
            });

            window.location.replace("/users");
        }).catch(function (error) {
            var errorCode = error.code;
            var errorMessage = error.message;
            console.log(errorCode);
            console.log(errorMessage);
        });
    
    }
}

function writeData() {
    var user = firebase.auth().currentUser;

    var rootRef = firebase.database().ref();
    var storesRef = rootRef.child('users');
    var newUser = storesRef.push();
    console.log("displayName = " + user.displayName);
    newUser.set({
        name: user.displayName,
        email: user.email,
        id: user.uid
    });
}