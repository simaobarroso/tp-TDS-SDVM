import { View, Text, Image, TouchableOpacity  } from 'react-native';
import { StyleSheet, FlatList , ScrollView, Linking} from 'react-native';
import React, { useEffect, useState, useContext } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { updateUsername } from '../actions/user';
import { updateAppInfo, setTrails } from '../actions/appData';
import { useNavigation } from '@react-navigation/native';
import {cores, api, api2} from '../var.js'
import MapView, { MapUrlTile, Marker, Polyline } from 'react-native-maps';
import Icon from 'react-native-vector-icons/MaterialIcons';
import Video from 'react-native-video';
import Sound from 'react-native-sound';



const Pin = ({ route }) => {
  const { pin_id, trail_id } = route.params;

  const [trail, setTrail] = useState("Loading!");

    const trails  = useSelector(state => state.data.appData.trails);


    const getTrail = async (trail_id) => {
      

        setTrail(trails[trail_id-1]);
      };
  
      useEffect(() => {
        getTrail(trail_id);
      }, [trail_id, trails]);

  
      const transformedData = trail && trail.edges && trail.edges.length > 0 
  ? [trail.edges[0].edge_start, ...trail.edges.map(edge => edge.edge_end)] 
  : [];

  const pin = transformedData.filter(edge => edge.id == pin_id)[0]

  useEffect(() => {
    console.log('Pin updated:', pin);
  }, [pin, pin_id]);

  console.log(pin)
  if (pin) console.log(pin.pin_name)

  

    const [startTime, setStartTime] = useState(null);
    const [isToggled, setToggle] = useState(false);
    const [visitedTrips, setVisitedTrips] = useState([]);
    const [notifications, setNotifications] = useState([]);

    const handleButtonToggle = () => {
      setToggle(!isToggled);
    };

    if (!pin) {
      return null; // or return a fallback UI
    }

    let imageUri = null
    let audioUri = null
    let videoUri = null

    const image = pin.media.filter(x => x.media_type == 'I')[0]
    if(image) imageUri = image.media_file
    //console.log(image)

    const audio = pin.media.filter(x => x.media_type == 'R')[0]
    if(audio) audioUri = audio.media_file

    const video = pin.media.filter(x => x.media_type == 'V')[0]
    if(video) videoUri = video.media_file


    const playAudio = () => {
      if (audioUri) {
        const sound = new Sound(audioUri, Sound.MAIN_BUNDLE, (error) => {
          if (error) {
            console.log('Failed to load the sound', error);
            return;
          }
          // Play the sound with an onEnd callback
          sound.play((success) => {
            if (success) {
              console.log('Successfully finished playing');
            } else {
              console.log('Playback failed due to audio decoding errors');
            }
          });
        });
      }
    };

  return (
    <ScrollView style={styles.container}>
      <View style={styles.container}>
        <View style={styles.containerCenter}>
          <Text style={styles.title}>{pin.pin_name}</Text>

          {imageUri && (
          <Image
            source={{ uri: imageUri }}
            style={styles.image}
            onError={() => console.log("Error loading image")}
          />
        )}

          <Text style={styles.desc}>{pin.pin_desc}</Text>

          {videoUri && (
          <Video
            source={{ uri: videoUri }}
            style={styles.video}
            controls={true}
            resizeMode="contain"
            onError={(error) => console.log("Error loading video:", error)}
          />
        )}

        {audioUri && (
          <TouchableOpacity style={styles.button} onPress={() => playAudio()}>
            <Text style={styles.buttonText}>Play Audio</Text>
          </TouchableOpacity>
        )}

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
    width: '47%',
    marginBottom: 85
  },
  buttonText: {
    paddingHorizontal: 10,
    paddingVertical: 8,
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
    height: 200,
    aspectRatio: 1,
    resizeMode: 'contain',
    marginBottom: 20,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginTop: 15,
    marginBottom: 30,
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
    },
    video: {
      width: '100%',
      height: 200,
      backgroundColor: '#000',
      marginVertical: 10,
    },
});

export default Pin;