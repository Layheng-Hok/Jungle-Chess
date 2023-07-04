package view;

public enum Themes {
    ArcDark("com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme"),
    ArcDarkOrange("com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme"),
    Arc("com.formdev.flatlaf.intellijthemes.FlatArcIJTheme"),
    ArcOrange("com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme"),
    Carbon("com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme"),
    Cobalt2("com.formdev.flatlaf.intellijthemes.FlatCobalt2IJTheme"),
    CyanLight("com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme"),
    DarkFlat("com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme"),
    DarkPurple("com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme"),
    Dracula("com.formdev.flatlaf.intellijthemes.FlatDraculaIJTheme"),
    GradiantoDarkFuchsia("com.formdev.flatlaf.intellijthemes.FlatGradiantoDarkFuchsiaIJTheme"),
    GradiantoDeepOcean("com.formdev.flatlaf.intellijthemes.FlatGradiantoDeepOceanIJTheme"),
    GradiantoMidnightBlue("com.formdev.flatlaf.intellijthemes.FlatGradiantoMidnightBlueIJTheme"),
    GradiantoNatureGreen("com.formdev.flatlaf.intellijthemes.FlatGradiantoNatureGreenIJTheme"),
    Gray("com.formdev.flatlaf.intellijthemes.FlatGrayIJTheme"),
    GruvboxDarkHard("com.formdev.flatlaf.intellijthemes.FlatGruvboxDarkHardIJTheme"),
    GruvboxDarkMedium("com.formdev.flatlaf.intellijthemes.FlatGruvboxDarkMediumIJTheme"),
    GruvboxDarkSoft("com.formdev.flatlaf.intellijthemes.FlatGruvboxDarkSoftIJTheme"),
    HiberbeeDark("com.formdev.flatlaf.intellijthemes.FlatHiberbeeDarkIJTheme"),
    HighContrast("com.formdev.flatlaf.intellijthemes.FlatHighContrastIJTheme"),
    LightFlat("com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme"),
    MaterialDesignDark("com.formdev.flatlaf.intellijthemes.FlatMaterialDesignDarkIJTheme"),
    Monokai("com.formdev.flatlaf.intellijthemes.FlatMonocaiIJTheme"),
    MonokaiPro("com.formdev.flatlaf.intellijthemes.FlatMonokaiProIJTheme"),
    Nord("com.formdev.flatlaf.intellijthemes.FlatNordIJTheme"),
    Dark("com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme"),
    SolarizedDark("com.formdev.flatlaf.intellijthemes.FlatSolarizedDarkIJTheme"),
    SolarizedLight("com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme"),
    Spacegray("com.formdev.flatlaf.intellijthemes.FlatSpacegrayIJTheme"),
    Vuesion("com.formdev.flatlaf.intellijthemes.FlatVuesionIJTheme"),
    XcodeDark("com.formdev.flatlaf.intellijthemes.FlatXcodeDarkIJTheme");

    private final String themeClassName;

    Themes(String themeClassName) {
        this.themeClassName = themeClassName;
    }

    public String getThemeClassName() {
        return themeClassName;
    }
}
