// self.addEventListener('push', function(event) {
//     const options = {
//         body: event.data.text(),
//     };
//
//     event.waitUntil(
//         self.registration.showNotification('Push Notification', options)
//     );
// });


self.addEventListener('push', function(event) {
    const options = {
        title: event.data.title(),
        body: event.data.body,
    };

    event.waitUntil(
        self.registration.showNotification(options.title, options.body)
    );
});
