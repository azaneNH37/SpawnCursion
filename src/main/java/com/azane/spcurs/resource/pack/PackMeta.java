package com.azane.spcurs.resource.pack;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PackMeta
{
    @SerializedName("name")
    private String name = "Unknown Pack";

    @SerializedName("version")
    private String version = "1.0.0";

    @SerializedName("description")
    private String description = "No description provided";

    @SerializedName("author")
    private String author = "Unknown Author";

    @SerializedName("dependencies")
    private HashMap<String, String> dependencies = new HashMap<>();

    @SerializedName("min_spcurs_version")
    private String minSpCursVersion = "0.1.0";

    public PackMeta(String name, String version, String description, String author) {
        this.name = name;
        this.version = version;
        this.description = description;
        this.author = author;
    }

    @Override
    public String toString() {
        return "PackMeta{" +
            "name='" + name + '\'' +
            ", version='" + version + '\'' +
            ", description='" + description + '\'' +
            ", author='" + author + '\'' +
            ", dependencies=" + dependencies +
            ", minSpCursVersion='" + minSpCursVersion + '\'' +
            '}';
    }
}
