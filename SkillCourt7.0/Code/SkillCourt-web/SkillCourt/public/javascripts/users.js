var db;
var openForms = 0;

function loadDB() {
    var temp = firebase.database().ref().once('value').then(function (snapshot) {
        db = snapshot.val();
        var user = firebase.auth().currentUser;
        loadUser(user);
        
    });
}


function logData() {
    console.log(db);
}

function loadUser(user) {
    console.log("loadUser: ");
    console.log(user);
    document.getElementById("name").innerHTML = user.displayName;
}   

function logout() {
    firebase.auth().signOut().then(function () {
        console.log("User Successfully logged out");
        MyAccountRedirect();
    }).catch(function (error) {
        console.log(error);
    });
}


function changePassword() {

    if (openForms > 0) {
        var forms = document.getElementById('forms');
        while (forms.firstChild) {
            forms.removeChild(forms.firstChild);
        }
        openForms--;
        return;
    }
    
    var forms = document.getElementById('forms');

    var div = document.createElement('div');
    div.setAttribute('class', 'loginmodal-container');

    var newForm = document.createElement('form');

    var password = document.createElement('input');
    password.setAttribute('type', 'password');
    password.setAttribute('placeholder', 'Current Password');
    password.setAttribute('id', 'currentPassword');

    var newPassword = document.createElement('input');
    newPassword.setAttribute('type', 'password');
    newPassword.setAttribute('placeholder', 'New Password');
    newPassword.setAttribute('id', 'newPassword');

    var confirmNewPassword = document.createElement('input');
    confirmNewPassword.setAttribute('type', 'password');
    confirmNewPassword.setAttribute('placeholder', 'Confirm New Password');
    confirmNewPassword.setAttribute('id', 'confirmPassword');

    var submitButton = document.createElement('button');
    submitButton.setAttribute('type', 'button');
    submitButton.setAttribute('onclick', 'submitChangePassword()');
    submitButton.innerHTML = "Submit";

    var disclaimer1 = document.createElement('p');
    disclaimer1.innerHTML = 'This is for standard Email/Password users';
    

    var disclaimer2 = document.createElement('p');
    disclaimer2.innerHTML = 'Users logged in via Facebook must manage their account through Facebook.com';

    newForm.appendChild(password);
    newForm.appendChild(newPassword);
    newForm.appendChild(confirmNewPassword);
    newForm.appendChild(submitButton);
    div.appendChild(newForm);
    forms.appendChild(div);
    forms.appendChild(disclaimer1);
    forms.appendChild(disclaimer2);

    openForms++;
}

function submitChangePassword() {
    var password = document.getElementById('currentPassword').value;
    var newPassword = document.getElementById('newPassword').value;
    var confirmPassword = document.getElementById('confirmPassword').value;

    if (!password || !newPassword || !confirmPassword) {
        alert("Please fill out all fields before submitting");
        return;
    }

    if (newPassword == confirmPassword) {
        var user = firebase.auth().currentUser;
        var credential = firebase.auth.EmailAuthProvider.credential(
            user.email,
            password
        );
        user.reauthenticate(credential).then(function () {
            user.updatePassword(newPassword).then(function () {
                alert("Your password has been changed");
                var forms = document.getElementById('forms');
                while (forms.firstChild) {
                    forms.removeChild(forms.firstChild);
                }
                openForms--;
            }, function (error) {
                console.log(error);
            });
        }, function (error) {
            alert("Password change failed, check current password and try again. " + 
                "Remember: password change is only an available " +
                "feature for users not logged in via Facebook");
            console.log(error);
        });

    } else {
        alert("Passwords do not match");
    }
}




function MyAccountRedirect() {
    var user = firebase.auth().currentUser;
    console.log(user);
    if (user) {
        window.location.replace("/users");
    } else {
        window.location.replace("/signin");
    }
}

















