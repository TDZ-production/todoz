self.addEventListener('push', function(event) {
    const data = event.data.json();

    const options = {
        body: data.body,
        data: {
            click_action: "/"
        }
    };

    event.waitUntil(
        self.registration.showNotification(data.title, options)
    );
});

self.addEventListener('notificationclick', function(event) {
    event.notification.close();

    event.waitUntil(
        clients.openWindow(event.notification.data.click_action)
    );
});
