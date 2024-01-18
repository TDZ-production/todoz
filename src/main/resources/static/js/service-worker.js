self.addEventListener('push', function(event) {

    const data = event.data.json()

    event.waitUntil(
        self.registration.showNotification(data.title, {body: data.body})
    );
});
