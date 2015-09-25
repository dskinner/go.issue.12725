package client

import "testing"

func TestFetch(t *testing.T) {
	items, err := Get()
	if err != nil {
		t.Fatal(err)
	}
	if items.Len() == 0 {
		t.Fatal("item length 0")
	}
	for _, e := range items.p {
		t.Logf("%+v\n", e)
	}
}
