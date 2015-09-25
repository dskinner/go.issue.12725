package client // import "dasa.cc/issue12725/client"

import (
	"encoding/xml"
	"net/http"
)

type Item struct {
	Title string `xml:"title"`
	Descr string `xml:"description"`
	Link  string `xml:"link"`
}

type Items struct {
	p []*Item
}

func (it *Items) Len() int        { return len(it.p) }
func (it *Items) Get(i int) *Item { return it.p[i] }

func Get() (*Items, error) {
	resp, err := http.Get("https://www.reddit.com/r/golang.rss")
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()
	rss := struct {
		P []*Item `xml:"channel>item"`
	}{}
	if err := xml.NewDecoder(resp.Body).Decode(&rss); err != nil {
		return nil, err
	}
	return &Items{rss.P}, nil
}
