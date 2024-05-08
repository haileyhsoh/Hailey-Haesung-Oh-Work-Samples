function checkIfRegistered() {
	var registeredEvents = JSON.parse(localStorage.getItem('registeredEvents'));
	var eventTitle = document.getElementById('event-title').textContent.trim();
	var text = document.getElementById('have-registered');

	if (registeredEvents && registeredEvents.some(event => event.event_name === eventTitle)) {
		text.style.display = 'block';
	} else {
		text.style.display = 'none';
	}
}


window.onload = checkIfRegistered;
/**
 * 
 */