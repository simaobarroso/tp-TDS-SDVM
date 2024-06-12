import { View, Text  } from 'react-native';
import MapView, { Marker, Polyline } from 'react-native-maps';


export const MapComponent = ({ data }) => {
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