import { View, Text, Image, Alert, TouchableOpacity ,PermissionsAndroid } from 'react-native';
import { StyleSheet, FlatList , ScrollView, Linking} from 'react-native';
import React, { useEffect, useState, useContext } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { updateUsername } from '../actions/user';
import { updateAppInfo, setTrails } from '../actions/appData';
import { useNavigation } from '@react-navigation/native';
import {cores, api, api2} from '../var.js'
import MapView, { Marker, Polyline } from 'react-native-maps';
import Icon from 'react-native-vector-icons/MaterialIcons';
import startLocationTracking from '../backgroundServices/Tracker.js';
import Geolocation from '@react-native-community/geolocation';
import PushNotification from 'react-native-push-notification';


const MapComponent = ({ data }) => {
    if (!data || data.length === 0) {
      return <Text>No map data available</Text>;
    }

  return (
    <MapView
      style={styles.map}
      initialRegion={{
        latitude: 41.55008,
        longitude: -8.4268,
        latitudeDelta: 0.1,
        longitudeDelta: 0.1,
      }}
    >
      {data.map((edge, index) => {
        if (!edge || !edge.edge_start || !edge.edge_end) return null;

        const start = edge.edge_start;
        const end = edge.edge_end;
        
        return (
          <View key={index}>
            <Marker
              coordinate={{ latitude: start.pin_lat, longitude: start.pin_lng }}
              title={start.pin_name}
              description={start.pin_desc}
            />
            <Marker
              coordinate={{ latitude: end.pin_lat, longitude: end.pin_lng }}
              title={end.pin_name}
              description={end.pin_desc}
            />
            <Polyline
              coordinates={[
                { latitude: start.pin_lat, longitude: start.pin_lng },
                { latitude: end.pin_lat, longitude: end.pin_lng }
              ]}
              strokeColor="#000"
              strokeWidth={3}
            />
          </View>
        );
      })}
    </MapView>
  );
};


const Trail = ({ route }) => {
  const { trail_id } = route.params;

    
  const [trail, setTrail] = useState("Loading!");

  const trails  = useSelector(state => state.data.appData.trails);

  const navigation = useNavigation();


    console.log(trail_id)

    const getTrail = async (trail_id) => {
      

      setTrail(trails[trail_id-1]);
    };

    useEffect(() => {
      getTrail(trail_id);
    }, [trail_id, trails]);
  

    const [startTime, setStartTime] = useState(null);
    const [isToggled, setToggle] = useState(false);
    const [visitedTrips, setVisitedTrips] = useState([]);
    const [notifications, setNotifications] = useState([]);

    const handleButtonToggle = () => {
      setToggle(!isToggled);
    };

    data = trail.edges;

    const [isNavigating, setIsNavigating] = useState(false);

    const handleNavigationPress = () => {
      if (isNavigating) {
        setIsNavigating(false);
      } else {
        openGoogleMaps();
        setIsNavigating(true);
      }
    };


    const calcDifficulty = (dif) => {
      if (dif === 'E') {
        return 'Easy';
      } else {
        return 'pao'; 
      }
    };

    const openGoogleMaps = () => {


      if (!data || data.length === 0) return;
    
      const coordinates = data
        .filter(edge => edge && edge.edge_start && edge.edge_end)
        .map(edge => ({
          start: `${edge.edge_start.pin_lat},${edge.edge_start.pin_lng}`,
          end: `${edge.edge_end.pin_lat},${edge.edge_end.pin_lng}`
        }));
      
      const waypoints = coordinates.map(coord => coord.start).join('|');
      console.log(waypoints)
      console.log(coordinates)
      const destination = coordinates[coordinates.length - 1].end;
      const url = `https://www.google.com/maps/dir/?api=1&destination=${destination}&waypoints=${waypoints}`;
    
      Linking.openURL(url);
    };

    
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  const requestLocationPermission = async () => {
    if (Platform.OS === 'android') {
      try {
        const granted = await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
          {
            title: 'Location Permission',
            message: 'App needs access to your location.',
            buttonNeutral: 'Ask Me Later',
            buttonNegative: 'Cancel',
            buttonPositive: 'OK',
          },
        );
        if (granted === PermissionsAndroid.RESULTS.GRANTED) {
          console.log('Location permission granted');
          // Proceed to start location updates
        } else {
          console.log('Location permission denied');
          // Handle permission denied case
        }
      } catch (err) {
        console.warn(err);
      }
    } else {
      // For iOS, handle permissions using CLLocationManager
      // Ensure proper keys are added in Info.plist
    }
  };

  requestLocationPermission();

  useEffect(() => {
    // Configure push notifications
    PushNotification.configure({
      onNotification: function (notification) {
        console.log("NOTIFICATION:", notification);
        // process the notification
      },
      requestPermissions: Platform.OS === 'ios',
    });

    // Create notification channel for Android
    PushNotification.createChannel(
      {
        channelId: "location-alerts", // Channel ID
        channelName: "Location Alerts", // Channel name
        channelDescription: "A channel to categorise location alerts", // Channel description
        soundName: "default",
        importance: 4, // Importance of the notifications in this channel
        vibrate: true, // Enable vibration
      },
      (created) => console.log(`CreateChannel returned '${created}'`) // (optional) callback returns whether the channel was created or not.
    );
  }, []);
  
  const transformedData = trail && trail.edges && trail.edges.length > 0 
  ? [trail.edges[0].edge_start, ...trail.edges.map(edge => edge.edge_end)] 
  : [];

  const showNotification = () => {
    PushNotification.localNotification({
      title: "Hello",
      message: "Your latitude is less than 50.",
    });
  };

  // Configure Geolocation
  Geolocation.setRNConfiguration({
    authorizationLevel: 'always', // Request "always" location permission
    skipPermissionRequests: false, // Prompt for permission if not granted
  });

// Watch for position updates
const watchId = Geolocation.watchPosition(
  position => {
    console.log(position);

    // TRATAR DE POR NOTIFICACAO AQUI
  },
  error => {
    console.log(error);
  },
  {
    distanceFilter: 10, // Minimum distance (in meters) to update the location
    interval: 1500, // Update interval (in milliseconds), which is 15 minutes
    fastestInterval: 1000, // Fastest update interval (in milliseconds)
    accuracy: {
      android: 'highAccuracy',
      ios: 'best',
    },
    showsBackgroundLocationIndicator: true,
    pausesLocationUpdatesAutomatically: false,
    activityType: 'fitness', // Specify the activity type (e.g., 'fitness' or 'other')
    useSignificantChanges: false,
    deferredUpdatesInterval: 0,
    deferredUpdatesDistance: 0,
    foregroundService: {
      notificationTitle: 'Tracking your location',
      notificationBody: 'Enable location tracking to continue', // Add a notification body
    },
  }
);

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    
  return (
    <ScrollView style={styles.container}>
      <View style={styles.container}>
        <View style={styles.containerCenter}>
          <Text style={styles.title}>{trail.trail_name}</Text>
          <Text style={styles.desc}>{trail.trail_desc}</Text>
        </View>

        <View style={styles.containerImageMap}>
          <View style={styles.imageContainer}>
            <Image 
              source={{ uri: trail.trail_img }} 
              style={styles.image}
              onError={() => console.log("Error loading image")}
            />
            <View style={styles.trailDescContainer}>
              <Icon size={15} color="black" name="access-time" style={styles.searchIcon}/>
              <Text style={styles.trailDuration}>{trail.trail_duration} min</Text>
            </View>
            <View style={styles.trailDescContainer}>
              <Icon size={15} color="black" name="trending-up" style={styles.searchIcon}/>
              <Text style={styles.trailDuration}>{calcDifficulty(trail.trail_difficulty)}</Text>
            </View>
          </View>
          <MapComponent data={data} style={styles.map} />
        </View>

        <View style={styles.containerButtons}>
        <TouchableOpacity style={styles.button} onPress={() => navigation.navigate('Edges', { trail_id: trail_id })}>
            <Text style={styles.buttonText}>See pins</Text>
          </TouchableOpacity>
          <TouchableOpacity style={styles.button} onPress={() => handleNavigationPress()}>
            <Text style={styles.buttonText}>{isNavigating ? 'Stop Trail' : 'Start Trail'}</Text>
          </TouchableOpacity>
        </View>

      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  button: {
    backgroundColor: cores.uminho, // Set your desired background color
    paddingHorizontal: 20,
    paddingVertical: 10,
    marginTop: 10,
    margin:10,
    borderRadius: 30,
    elevation: 1, // Add shadow on Android
    width: '45%',
    marginBottom: 85
  },
  buttonText: {
    paddingHorizontal: 20,
    paddingVertical: 10,
    color: 'white',
    textAlign: 'center',
  },
  container: {
    flex: 1,
    padding: 15,
    backgroundColor : 'white',

  },
  containerCenter: {
    justifyContent: 'center',
    alignItems: 'center',
  },
  containerButtons: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center'
  },
  containerImageMap: {
    flexDirection: 'row',
    alignItems: 'flex-start', // Align items to the start of the cross axis (vertically in this case)
    paddingVertical: 20,
  },
  imageContainer: {
    width: '50%', // Take half of the parent container's width
    marginRight: 15, // Spacing between image and map
  },
  image: {
    width: '100%',
    height: undefined,
    aspectRatio: 1,
    resizeMode: 'contain',
    marginBottom: 10,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginTop: 15,
    marginBottom: 20,
    color : cores.uminho
  },
  desc: {
    fontSize: 16,
    fontWeight: 'normal',
    marginBottom: 20,
    color : 'black'
  },
    map: {
      width: '40%',
      height: '100%', // Adjust the height here to make the map smaller
    },
    trailItem: {
      marginRight: 16,
      alignItems: 'center',
      height: 200,
      backgroundColor: '#FFF7F7',
      borderRadius: 15,
      elevation: 4
    },
    trailImage: {
      width: 150,
      height: 150,
      borderTopLeftRadius: 15,
      borderTopRightRadius: 15
    },
    trailName: {
      marginTop: 8,
      fontSize: 14,
      color : 'black',
      fontWeight: '600'
    },
    trailDescContainer: {
      marginTop: 2,
      flexDirection: 'row',
      justifyContent: 'center',
      alignItems: 'ceenter',
    },
    trailDuration: {
      paddingHorizontal: 3,
      fontSize: 12,
      color : 'black',
      fontWeight: '3~400'
    }
});

export default Trail;