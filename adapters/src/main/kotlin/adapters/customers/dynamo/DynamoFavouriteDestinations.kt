package adapters.dynamo;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.List;

@DynamoDbBean
public class DynamoFavouriteDestinations {

    private List<String> destinations;

    public DynamoFavouriteDestinations() {
    }

    public DynamoFavouriteDestinations(List<String> destinations) {
        this.destinations = destinations;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<String> destinations) {
        this.destinations = destinations;
    }
}
