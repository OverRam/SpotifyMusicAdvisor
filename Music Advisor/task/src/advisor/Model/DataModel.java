package advisor.Model;

import advisor.Model.SpotifyClass.Category;

import java.util.*;
import java.util.stream.Collectors;

public class DataModel {
    private final SpotifyApiResourceModel apiResourceModel = SpotifyApiResourceModel.getInstance();
    private Map<String, String> categoryMap;
    private List<List<String>> pages;
    private static int ENTRIES_PER_PAGE = 5;
    private int actualPage;

    public void setCategories() {
        categoryMap = apiResourceModel.getCategoriesFromServer()
                .stream().collect(Collectors.toMap(Category::getName, Category::getId));
    }

    public String getCategoryID(String nameCategory) {
        return categoryMap.get(nameCategory);
    }

    public List<String> getCategoryPage() {
        actualPage = 0;
        pages = splitListToPage(apiResourceModel.getCategoriesFromServer()).stream()
                .map(x -> x.stream().map(Category::getName).collect(Collectors.toList()))
                .collect(Collectors.toList());
        addNumbersToPages();

        return pages.get(actualPage);
    }

    /**
     * @param mode = next or prev its just next page or prev page
     * @return the requested page or "No more pages." as list if you go over the extreme position
     */
    public List<String> getOtherPage(String mode) {
        switch (mode) {
            case "next": {
                if (actualPage < pages.size() - 1) {
                    ++actualPage;
                    return pages.get(actualPage);
                }
                return List.of("No more pages.");
            }
            case "prev": {
                if (actualPage > 0) {
                    --actualPage;
                    return pages.get(actualPage);
                }
                return List.of("No more pages.");
            }
            default: {
                return null;
            }
        }
    }

    public List<String> getNewSongs() {
        actualPage = 0;
        pages = splitListToPage(apiResourceModel.getNewSongsFromServer())
                .stream()
                .map(x -> x.stream().map(s -> {
                    List<String> artists = new ArrayList<>();
                    Arrays.stream(s.getArtists()).iterator().forEachRemaining(a -> artists.add(a.getName()));

                    return s.getName() + "\n" +
                            Arrays.toString(artists.toArray()) + "\n" +
                            s.getExternal_urls().getUrl() + "\n";

                }).collect(Collectors.toList()))
                .collect(Collectors.toList());

        addNumbersToPages();
        return pages.get(actualPage);
    }

    public List<String> getPlaylistsPage(String categoryId) {
        actualPage = 0;

        pages = splitListToPage(apiResourceModel.getPlaylistByCategoryID(categoryId))
                .stream()
                .map(x -> x.stream().map(s -> s.getName() + "\n" + s.getExternal_urls().getUrl() + "\n"
                ).collect(Collectors.toList()))
                .collect(Collectors.toList());

        addNumbersToPages();

        return pages.get(actualPage);
    }

    public void setEntriesPerPage(int numOfEntries) {
        ENTRIES_PER_PAGE = numOfEntries;
    }

    private void addNumbersToPages() {
        for (int i = 0; i < pages.size(); i++) {
            pages.get(i).add("---PAGE " + (i + 1) + " OF " + pages.size() + "---");
        }
    }

    private <T> List<List<T>> splitListToPage(List<T> list) {
        List<List<T>> listOfType = new ArrayList<>();
        int maxBound;

        for (int i = 0; i < list.size(); i += ENTRIES_PER_PAGE) {
            maxBound = i + ENTRIES_PER_PAGE;

            if (maxBound > list.size() - 1) {
                maxBound = list.size();
            }

            listOfType.add(new ArrayList<>(list.subList(i, maxBound)));
        }

        return listOfType;
    }

}
