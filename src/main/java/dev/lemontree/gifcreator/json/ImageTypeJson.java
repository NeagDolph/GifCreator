package dev.lemontree.gifcreator.json;


public record ImageTypeJson(ImageDataJson original, ImageDataJson fixed_height, ImageDataJson fixed_width,
                            ImageDataJson fixed_height_downsampled, ImageDataJson fixed_height_small, ImageDataJson fixed_width_downsampled, ImageDataJson fixed_width_small) {
}

