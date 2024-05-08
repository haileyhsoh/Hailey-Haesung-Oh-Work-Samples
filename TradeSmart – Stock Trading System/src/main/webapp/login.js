

document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("login-submit-button").addEventListener("click", function(event) {
        event.preventDefault(); 
        validateLogin();
    });

    document.getElementById("signup-submit-button").addEventListener("click", function(event) {
        event.preventDefault(); 
        validateSignUp();
    });
});


function validateSignUp() {
    const email = document.getElementById("signup-email").value;
    const username = document.getElementById("signup-username").value;
    const password = document.getElementById("signup-password").value;
    const balance = 50000;

    fetch('RegisterServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json', 
        },
        body: JSON.stringify({ 
            email: email,
            username: username,
            password: password,
            balance: balance
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok.');
        }
        return response.json();
    })
    .then(data => {
        signupFunction(data);
    })
    .catch((error) => {
        console.error('Fetch operation:', error);
        document.getElementById("result").textContent = 'Error fetching data.';
    });
}


function signupFunction(data) { 
	
	if (data == "User info missing" || data == "Username is taken" || data == "Email is already registered.") {
		alert(data);
	} else {
		//alert("User ID is " + data);
		window.localStorage.setItem("logged-in", "yes");
		window.localStorage.setItem("userID", data);
		window.location.href = 'index.html';
	}
	

	}

function validateLogin() {
		
	const username = document.getElementById("login-username").value;
	const password = document.getElementById("login-password").value;
	
	
	fetch('LoginServlet', {
	method: 'POST',
	headers: {
		'Content-Type': 'application/x-www-form-urlencoded',
	},
	body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`
})

		.then(response => {
			if (!response.ok) {
				throw new Error('Network response was not ok.');
			}
			return response.json();
		})
		.then(data => {
			loginFunction(data);
			
		})
		.catch((error) => {
			console.error('Fetch operation:', error);
			document.getElementById("result").textContent = 'Error fetching data.';
		});
}

function loginFunction(data) {
	
	
	
	window.localStorage.setItem("logged-in", "yes");
	window.localStorage.setItem("username", data.username);
	window.location.href = 'index.html';
	
	
	
	
}