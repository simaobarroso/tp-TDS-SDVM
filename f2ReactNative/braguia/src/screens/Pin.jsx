import { View, Text, Image, TouchableOpacity  } from 'react-native';
import { StyleSheet, FlatList , ScrollView, Linking, Alert} from 'react-native';
import React, { useEffect, useState, useContext } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { updateUsername } from '../actions/user';
import { updateAppInfo, setTrails } from '../actions/appData';
import { useNavigation } from '@react-navigation/native';
import {cores, api, api2} from '../var.js'
import MapView, { MapUrlTile, Marker, Polyline } from 'react-native-maps';
import Icon from 'react-native-vector-icons/MaterialIcons';
import RNFS from 'react-native-fs';
import Permissions from 'react-native-permissions';
import Video from 'react-native-video';
import Sound from 'react-native-sound';

const MapComponent = ({ data , style}) => {
  // Check if data is provided and is an object with pin information
  if (!data || !data.pin_lat || !data.pin_lng ) {
    return <Text>No valid pin data available</Text>;
  }

  const { pin_lat, pin_lng } = data;

  console.log(data)

  return (
    <MapView
      style = {style}
      initialRegion={{
        latitude: pin_lat,
        longitude: pin_lng,
        latitudeDelta: 0.01,
        longitudeDelta: 0.01,
      }}
    >
      <Marker
        coordinate={{ latitude: pin_lat, longitude: pin_lng }}
      />
    </MapView>
  );
};

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


    const downloadFile = async (uri, fileType) => {
      if (!uri) return;
  
      const permission = await Permissions.request(Permissions.PERMISSIONS.ANDROID.WRITE_EXTERNAL_STORAGE);
  
      if (permission === Permissions.RESULTS.GRANTED) {
        const fileName = uri.split('/').pop();
        const filePath = `${RNFS.DownloadDirectoryPath}/${fileName}`;
  
        RNFS.downloadFile({
          fromUrl: uri,
          toFile: filePath,
        }).promise.then(res => {
          if (res.statusCode === 200) {
            Alert.alert('Download Success', `${fileType} downloaded successfully!`);
          } else {
            Alert.alert('Download Failed', `Failed to download ${fileType}`);
          }
        }).catch(err => {
          console.log(err);
          Alert.alert('Download Error', `An error occurred while downloading the ${fileType}`);
        });
      } else if (permission === Permissions.RESULTS.DENIED) {
        Alert.alert(
          'Permission Denied',
          'You need to give storage permission to download the file',
          [
            { text: 'Cancel', style: 'cancel' },
            { text: 'Open Settings', onPress: () => Linking.openSettings() },
          ],
        );
      } else if (permission === Permissions.RESULTS.BLOCKED) {
        Alert.alert(
          'Permission Blocked',
          'Storage permission is blocked. Please open settings to enable it.',
          [
            { text: 'Cancel', style: 'cancel' },
            { text: 'Open Settings', onPress: () => Linking.openSettings() },
          ],
        );
      }
    };
  
    const downloadMedia = () => {
      if (imageUri) downloadFile(imageUri, 'Image');
      if (audioUri) downloadFile(audioUri, 'Audio');
      if (videoUri) downloadFile(videoUri, 'Video');
    };


    console.log('Using style:', imageUri ? styles.map : styles.abc);

  return (
    <ScrollView style={styles.container}>
      <View style={styles.container}>
        <View style={styles.containerCenter}>
          <Text style={styles.title}>{pin.pin_name}</Text>


            {imageUri ? (
            <View style={styles.containerImageMap}>
              <View style={styles.imageContainer}>
                <Image
                  source={{ uri: imageUri }}
                  style={styles.image}
                  onError={() => console.log("Error loading image")}
                />
            </View>
            <MapComponent
              data={{
                pin_lat: pin.pin_lat,
                pin_lng: pin.pin_lng,
              }}
              style={styles.map}
              />
            </View>
          ):
          <MapComponent
              data={{
                pin_lat: pin.pin_lat,
                pin_lng: pin.pin_lng,
              }}
              style={styles.abc}
              />
          }


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

        <View style={ styles.containerButtons}>
          {audioUri && (
            <TouchableOpacity style={styles.button} onPress={() => playAudio()}>
              <Text style={styles.buttonText}>Play Audio</Text>
            </TouchableOpacity>
          )}
          <TouchableOpacity style={styles.button} onPress={() => downloadMedia()} >
              <Text style={styles.buttonText}>Download Media</Text>
            </TouchableOpacity>
        </View>

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
    marginBottom: 30,
    color : cores.uminho
  },
  desc: {
    fontSize: 16,
    fontWeight: 'normal',
    marginBottom: 20,
    color : 'black'
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
    map: {
      width: '40%',
      height: '100%'
    },
    abc: {
      marginBottom: 25,
      width: '80%',
      height: 200
    },
});

export default Pin;