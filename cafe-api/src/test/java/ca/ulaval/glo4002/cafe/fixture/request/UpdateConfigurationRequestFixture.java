package ca.ulaval.glo4002.cafe.fixture.request;

import ca.ulaval.glo4002.cafe.api.configuration.request.UpdateConfigurationRequest;

public class UpdateConfigurationRequestFixture {
    public String group_reservation_method = "Default";
    public String organization_name = "Les 4-Fées";
    public String country = "CA";
    public String province = "QC";
    public String state = "";
    public int cube_size = 4;
    public float group_tip_rate;

    public UpdateConfigurationRequestFixture withGroupReservationMethod(String groupReservationMethod) {
        this.group_reservation_method = groupReservationMethod;
        return this;
    }

    public UpdateConfigurationRequestFixture withOrganizationName(String organizationName) {
        this.organization_name = organizationName;
        return this;
    }

    public UpdateConfigurationRequestFixture withCubeSize(int cubeSize) {
        this.cube_size = cubeSize;
        return this;
    }

    public UpdateConfigurationRequestFixture withCountry(String country) {
        this.country = country;
        return this;
    }

    public UpdateConfigurationRequestFixture withProvince(String province) {
        this.province = province;
        return this;
    }

    public UpdateConfigurationRequestFixture withState(String state) {
        this.state = state;
        return this;
    }

    public UpdateConfigurationRequestFixture withTipRate(float tipRate) {
        this.group_tip_rate = tipRate;
        return this;
    }

    public UpdateConfigurationRequest build() {
        UpdateConfigurationRequest updateConfigurationRequest = new UpdateConfigurationRequest();
        updateConfigurationRequest.group_reservation_method = group_reservation_method;
        updateConfigurationRequest.organization_name = organization_name;
        updateConfigurationRequest.cube_size = cube_size;
        updateConfigurationRequest.country = country;
        updateConfigurationRequest.state = state;
        updateConfigurationRequest.province = province;
        updateConfigurationRequest.group_tip_rate = group_tip_rate;
        return updateConfigurationRequest;
    }
}
