// Index JS file
document.addEventListener('DOMContentLoaded', () => {
    const isLoggedIn = checkLoginStatus();
    let userCashAvailable = 1000; // Example cash available, you'll need to retrieve this from your user data
    
    populateNavigationMenu(isLoggedIn);
    
    const searchButton = document.getElementById('search-button');
    const searchInput = document.getElementById('search-input');

    searchButton.addEventListener('click', () => {
        const event = searchInput.value.toUpperCase();
        if (!event) {
            alert("Please enter a event name to get its information.");
            return;
        }
        if (isLoggedIn) {
	        getUserEventData(event)
	            .then(eventData => {
	                if (eventData) {
	                    displayEventDetails(eventData, isLoggedIn);
	                    addBuyButtonListener(stockData, userCashAvailable);
	                } else {
	                    alert("Information not found for event" + event + ".");
	                }
	            })
	            .catch(error => {
	                alert("Failed to fetch stock data for ticker " + ticker + ".");
	                console.error('Fetch error: ', error);
	            });
	      }
	      else {
			  getGuestStockData(ticker)
	            .then(stockData => {
	                if (stockData) {
	                    displayStockDetails(stockData, isLoggedIn);
	                } else {
	                    alert("Stock data not found for ticker " + ticker + ".");
	                }
	            })
	            .catch(error => {
	                alert("Failed to fetch stock data for ticker " + ticker + ".");
	                console.error('Fetch error: ', error);
	            });
			  
		  }
     });
 });