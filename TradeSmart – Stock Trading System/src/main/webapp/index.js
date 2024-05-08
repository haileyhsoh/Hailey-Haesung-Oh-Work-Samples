window.onload = function() {
	if (window.localStorage.getItem("logged-in") == "yes") {
		var loginOrLogoutDiv = document.getElementById('login-or-logout');
		loginOrLogoutDiv.innerHTML = '';

		var portfolioLink = document.createElement('a');
		portfolioLink.href = 'portfolio.html';
		portfolioLink.textContent = 'Portfolio';

		var logoutLink = document.createElement('a');
		logoutLink.href = '#';
		logoutLink.textContent = 'Logout';
		logoutLink.onclick = function() {
			window.localStorage.setItem("logged-in", "no");
			window.location.reload();
		}

		loginOrLogoutDiv.appendChild(portfolioLink);
		loginOrLogoutDiv.appendChild(logoutLink);
	} 
}

// ChatGPT, 29 lines
document.addEventListener("DOMContentLoaded", function() {
	document.getElementById("search-button").addEventListener("click", function() {
		searchStocks();
	});
});

function searchStocks() {
	const ticker = document.getElementById("search-box").value;

	if (!ticker) {
		alert("Please enter a ticker.");
		return;
	}
	fetch(`StockServlet?ticker=${ticker}`, {
		method: 'GET'
	})
		.then(response => {
			if (!response.ok) {
				throw new Error('Network response was not ok.');
			}
			return response.json();
		})
		.then(data => {
			displayResults(data);
		})
		.catch((error) => {
			console.error('There was a problem with your fetch operation:', error);
			document.getElementById("result").textContent = 'Error fetching data.';
		});
}
function displayResults(data) {
	document.body.innerHTML = document.getElementById('navbar').outerHTML; // Clear the page except for the navbar
	const result = document.createElement('div');

	if (window.localStorage.getItem("logged-in") == "no") {
		result.style.display = 'flex';
		result.style.justifyContent = 'center';
		result.style.alignItems = 'center';
		result.style.flexDirection = 'column';

		const ticker = document.createElement('h2');
		ticker.textContent = data.profile.ticker;
		result.appendChild(ticker);

		const name = document.createElement('p');
		name.textContent = data.profile.name;
		result.appendChild(name);

		const exchange = document.createElement('p');
		exchange.textContent = data.profile.exchange;
		result.appendChild(exchange);

		const summary = document.createElement('p');
		summary.textContent = "Summary";
		result.appendChild(summary);

		const line = document.createElement('hr');
		line.style.border = '1px solid gray';
		line.style.width = '50%';
		result.appendChild(line);

		const high = document.createElement('p');
		high.textContent = "High Price: " + data.quote.h;
		result.appendChild(high);

		const low = document.createElement('p');
		low.textContent = "Low Price: " + data.quote.l;
		result.appendChild(low);

		const open = document.createElement('p');
		open.textContent = "Open Price: " + data.quote.o;
		result.appendChild(open);

		const close = document.createElement('p');
		close.textContent = "Close Price: " + data.quote.pc;
		result.appendChild(close);


		const line2 = document.createElement('hr');
		line2.style.border = '1px solid gray';
		line2.style.width = '50%';
		result.appendChild(line2);


		const companyInfo = document.createElement('h3');
		companyInfo.textContent = "Company Information";
		result.appendChild(companyInfo);

		const info = document.createElement('div');
		info.style.alignItems = 'flex-start';
		info.style.display = 'flex';
		info.style.flexDirection = 'column';
		info.style.width = '50%';

		const ipo = document.createElement('p');
		ipo.innerHTML = '<strong>IPO Date: </strong>' + data.profile.ipo;
		info.appendChild(ipo);

		const market = document.createElement('p');
		market.innerHTML = "<strong>Market Cap ($M): </strong>" + data.profile.marketCapitalization;
		info.appendChild(market);

		const share = document.createElement('p');
		share.innerHTML = "<strong>Share Outstanding: </strong>" + data.profile.shareOutstanding;
		info.appendChild(share);

		const website = document.createElement('p');
		website.innerHTML = "<strong>Website: </strong>" + data.profile.weburl;
		info.appendChild(website);

		const phone = document.createElement('p');
		phone.innerHTML = "<strong>Phone: </strong>" + data.profile.phone;
		info.appendChild(phone);

		result.appendChild(info);
		document.body.appendChild(result);
	} else { // logged in

		const container = document.createElement('div');
		container.style.display = 'flex';
		container.style.justifyContent = 'space-around';
		container.style.alignItems = 'flex-start';
		container.style.marginTop = '20px';

		// Create the first div (div1) with left-aligned text
		const div1 = document.createElement('div');
		div1.style.width = '45%';
		div1.style.textAlign = 'left';

		// Example content for div1
		const div1Ticker = document.createElement('h3');
		div1Ticker.textContent = data.profile.ticker;
		div1.appendChild(div1Ticker);
		// Add more content to div1 as needed...

		const name = document.createElement('p');
		name.textContent = data.profile.name;
		div1.appendChild(name);

		const exchange = document.createElement('p');
		exchange.textContent = data.profile.exchange;
		div1.appendChild(exchange);


		const label = document.createElement('label');
		label.textContent = "Quantity:";
		div1.appendChild(label);

		const textbox = document.createElement('input');
		textbox.type = 'text';
		textbox.style.marginLeft = '10px';
		textbox.name = 'quantity';
		div1.appendChild(textbox);

		const buyButton = document.createElement('button');
		buyButton.style.backgroundColor = 'darkgreen'; // 버튼의 배경색을 어두운 녹색으로 설정
		buyButton.style.color = 'white';
		buyButton.style.marginTop = '10px';
		buyButton.style.display = 'block';
		buyButton.textContent = "Buy";
		buyButton.style.padding = '10px';
		buyButton.style.borderRadius = '5px';

		div1.appendChild(buyButton);

		// Create the second div (div2) with right-aligned text
		const div2 = document.createElement('div');
		div2.style.width = '45%';
		div2.style.textAlign = 'right';

		// Example content for div2
		const lastPrice = document.createElement('h2');
		lastPrice.textContent = data.quote.l;
		div2.appendChild(lastPrice);

		// ChatGPT - 10 lines
		const time = document.createElement('p');
		const now = new Date();
		const year = now.getFullYear();
		const month = (now.getMonth() + 1).toString().padStart(2, '0');
		const day = now.getDate().toString().padStart(2, '0');
		const hours = now.getHours().toString().padStart(2, '0');
		const minutes = now.getMinutes().toString().padStart(2, '0');
		const seconds = now.getSeconds().toString().padStart(2, '0');
		time.textContent = `${month}-${day}-${year} ${hours}:${minutes}:${seconds}`;


		const change = document.createElement('h2');

		if (data.quote.dp.toString().indexOf("-") == -1) {
			change.innerHTML = '<i class="fa-solid fa-caret-up"></i> ' + data.quote.d + " (" + data.quote.dp.toString() + "%)";
			change.style.color = 'green';
			lastPrice.style.color = 'green';
			time.style.color = 'green';

		} else {
			change.innerHTML = '<i class="fa-solid fa-caret-down"></i> ' + data.quote.d + " (" + data.quote.dp.toString() + "%)";
			change.style.color = 'red';
			lastPrice.style.color = 'red';
			time.style.color = 'red';

		}
		div2.appendChild(change);
		div2.appendChild(time);


		// Append both divs to the container
		container.appendChild(div1);
		container.appendChild(div2);
		
		document.body.appendChild(container);
		
		
		const div3 = document.createElement('div');		
		div3.style.textAlign = 'center';
		div3.style.marginTop = '20px';
		
		// ChatGPT - 7 lines
		const currentTime = new Date();
		const hour = currentTime.getHours();
		const minute = currentTime.getMinutes();
		const open = new Date(currentTime);
		open.setHours(6, 30, 0); 
		const close = new Date(currentTime);
		close.setHours(13, 0, 0);
		
		if (currentTime >= open && currentTime < close) {
		    div3.textContent = "Market is Open";
		} else {
		    div3.textContent = "Market is Closed";
		}
		
		document.body.appendChild(div3);
		
		
		const summary = document.createElement('p');
		summary.textContent = "Summary";
		div3.appendChild(summary);

		const line = document.createElement('hr');
		line.style.border = '1px solid gray';
		line.style.width = '95%';
		div3.appendChild(line);

		const high = document.createElement('p');
		high.innerHTML = "<strong>High Price: " + data.quote.h + "</strong>";
		div3.appendChild(high);

		const low = document.createElement('p');
		low.innerHTML = "<strong>Low Price: " + data.quote.l + "</strong>";
		div3.appendChild(low);

		const open2 = document.createElement('p');
		open2.innerHTML = "<strong>Open Price: " + data.quote.o + "</strong>";
		div3.appendChild(open2);

		const close2 = document.createElement('p');
		close2.innerHTML = "<strong>Close Price: " + data.quote.pc + "</strong>";
		div3.appendChild(close2);


		const line2 = document.createElement('hr');
		line2.style.border = '1px solid gray';
		line2.style.width = '95%';
		div3.appendChild(line2);


		const companyInfo = document.createElement('h3');
		companyInfo.textContent = "Company Information";
		div3.appendChild(companyInfo);

		const info = document.createElement('div');
		info.style.alignItems = 'flex-start';
		info.style.display = 'flex';
		info.style.flexDirection = 'column';
		info.style.width = '90%';
		info.style.marginLeft = '50px';

		const ipo = document.createElement('p');
		ipo.innerHTML = '<strong>IPO Date: </strong>' + data.profile.ipo;
		info.appendChild(ipo);

		const market = document.createElement('p');
		market.innerHTML = "<strong>Market Cap ($M): </strong>" + data.profile.marketCapitalization;
		info.appendChild(market);

		const share = document.createElement('p');
		share.innerHTML = "<strong>Share Outstanding: </strong>" + data.profile.shareOutstanding;
		info.appendChild(share);

		const website = document.createElement('p');
		website.innerHTML = "<strong>Website: </strong>" + data.profile.weburl;
		info.appendChild(website);

		const phone = document.createElement('p');
		phone.innerHTML = "<strong>Phone: </strong>" + data.profile.phone;
		info.appendChild(phone);

		div3.appendChild(info);



	}


}


