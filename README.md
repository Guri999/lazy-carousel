# Jetpack Compose Lazy Carousel
<a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>

Android library providing Carousel for JetPack Compose

smooth scroll similar to LazyRow

## Installation
Add it in your root build.gradle at the end of repositories:
```groovy
	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```
In your module build.gradle :
```groovy
dependencies {
  implementation "com.github.Guri999:lazy-carousel:0.1.1"
}
```

## Usage
### CenterZoomedCarousel
<img src="/previews/preview.gif" align="right" width="250"/>

```kotlin
CenterCarousel(
    modifier = Modifier.height(200.dp),
    horizontalArrangement = Arrangement.spacedBy(12.dp)
) {
    zoomedCarouselItems(
        items = pokemons,
        itemSize = itemSize,
        zoomItemSize = itemSize * 3f,
        scrollType = CarouselScrollType.INFINITE
    ) { item, zoomModifier, isFocus ->
        if (isFocus) name = item.second

        Card(elevation = CardDefaults.elevatedCardElevation()) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(12.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(item.first),
                    contentDescription = item.second,
                    modifier = zoomModifier
                )
            }
        }
    }
}
```
By using the `scrollType` you can enable either default scrolling or infinity scrolling

Pass any list of item to `items` parameter, and it will return a `item`, `zoomModifier`, `isFocus`
