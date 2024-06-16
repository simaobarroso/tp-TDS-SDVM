import Geolocation from '@react-native-community/geolocation';
import PushNotification from 'react-native-push-notification';

// Function to handle location updates
const handleLocationUpdate = (position) => {
  const { latitude, longitude } = position.coords;
  // Add your location-based logic here
  // Example: if user is near a specific location, send a notification
  if (isUserNearLocation(latitude, longitude)) {
    PushNotification.localNotification({
      message: 'You are near a location of interest!',
    });
  }
};

// Function to check if user is near a specific location
const isUserNearLocation = (latitude, longitude) => {
  // Replace with your target location
  const targetLatitude = 37.7749;
  const targetLongitude = -122.4194;
  const threshold = 0.01; // ~1 km
  return (
    Math.abs(latitude - targetLatitude) < threshold &&
    Math.abs(longitude - targetLongitude) < threshold
  );
};

// Start background location tracking
export const startLocationTracking = () => {
  Geolocation.watchPosition(
    handleLocationUpdate,
    (error) => console.error(error),
    {
      enableHighAccuracy: true,
      distanceFilter: 100,
      interval: 60000,
      fastestInterval: 5000,
    }
  );
};
