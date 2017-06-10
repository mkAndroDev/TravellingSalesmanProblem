package travellingsalesmanproblem;

import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import com.krawczyk.maciej.travellingsalesmanproblem.android.Utils.Utils
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.junit.Test
import java.util.*
import kotlin.test.assertTrue

class UtilsTest {

    @Test
    fun getAddressFromGeocoderShouldReturnValidAddress() {
        /* Given */
        val addressMock = mock<Address> {
            on { getAddressLine(0) } doReturn "Matejki 15"
            on { getLocality() } doReturn "Lodz"
        }
        val addresses = ArrayList<Address>()
        addresses.add(addressMock)
        val geocoderMock = mock<Geocoder> {
            on { getFromLocation(any(), any(), any()) } doReturn addresses
        }
        /* When */
        val addressFormGeocoder = Utils.getAddress(geocoderMock, LatLng(52.0, 19.0))
        /* Then */
        assertTrue { addressFormGeocoder == addressMock.getAddressLine(0) + ", " + addressMock.getLocality() }
    }
}
